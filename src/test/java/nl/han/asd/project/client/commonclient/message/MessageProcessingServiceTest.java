package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.graph.GraphManagerService;
import nl.han.asd.project.client.commonclient.node.NodeConnectionService;
import nl.han.asd.project.client.commonclient.store.*;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Jevgeni on 17-5-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageProcessingServiceTest {

    @Mock private MessageStore messageStore;
    @Mock private NodeConnectionService nodeConnectionService;
    @Mock private MessageConfirmationService messageConfirmationService;
    @Mock private ContactStore contactStore;
    @Mock private MessageBuilderService messageBuilder;
    @Mock private GraphManagerService graphManagerService;
    @Mock private ContactManager contactManager;

    private IEncryptionService encryptionService;

    private MessageProcessingService messageProcessingService;

    @Before public void initService() {
        final Injector injector = Guice.createInjector(new EncryptionModule());

        encryptionService = injector.getInstance(IEncryptionService.class);
        messageProcessingService = new MessageProcessingService(messageStore, encryptionService,
                nodeConnectionService, messageConfirmationService, contactStore, messageBuilder,
                graphManagerService, contactManager);
    }

    @Test public void testWithMessageWrapper() {
        HanRoutingProtocol.Message message = getMessage();

        CurrentUser currentUser = mock(CurrentUser.class);
        Contact contact = new Contact("receiver");

        when(contactStore.getCurrentUser()).thenReturn(currentUser);
        when(currentUser.asContact()).thenReturn(contact);

        messageProcessingService.processIncomingMessage(message);

        // verify that the addMessage is called (thus no exceptions where thrown)
        verify(messageStore, times(1)).addMessage(any(Message.class));
    }


    @Test public void testWithMessageConfirmationWrapper() {
        String confirmedId = "1111111";
        HanRoutingProtocol.MessageConfirmation messageConfirmation = HanRoutingProtocol.MessageConfirmation.newBuilder().setConfirmationId(confirmedId).build();

        messageProcessingService.processIncomingMessage(messageConfirmation);

        verify(messageConfirmationService).messageConfirmationReceived(eq(confirmedId));
    }

    @Test public void testInvalidType() {
        HanRoutingProtocol.GraphUpdateRequest.Builder graphUpdateRequestBuilder = HanRoutingProtocol.GraphUpdateRequest
                .newBuilder();
        graphUpdateRequestBuilder.setCurrentVersion(1);

        // build wrapper containing Type.GRAPHUPDATEREQUEST, processIncomingMessage should be unable to parse this.
        ByteString wrapperByteString = HanRoutingProtocol.Wrapper.newBuilder()
                .setData(graphUpdateRequestBuilder.build().toByteString()).setType(HanRoutingProtocol.Wrapper.Type.GRAPHUPDATEREQUEST)
                .build().toByteString();

        HanRoutingProtocol.MessageWrapper messageWrapper = getMessageWrapper(
                wrapperByteString);
        messageProcessingService.processIncomingMessage(messageWrapper);

        // verify that the addMessage or messageReceived method are never called.
        verify(messageStore, times(0)).addMessage(any(Message.class));
    }

    @Test public void testInvalidTypeWithValidTypeParameter() {
        HanRoutingProtocol.GraphUpdateRequest.Builder graphUpdateRequestBuilder = HanRoutingProtocol.GraphUpdateRequest
                .newBuilder();
        graphUpdateRequestBuilder.setCurrentVersion(1);

        // same as testInvalidType, but now we're not setting the type parameter to GraphUpdateRequest but to Message
        ByteString wrapperByteString = HanRoutingProtocol.Wrapper.newBuilder()
                .setData(graphUpdateRequestBuilder.build().toByteString()).setType(HanRoutingProtocol.Wrapper.Type.MESSAGE).build()
                .toByteString();

        HanRoutingProtocol.MessageWrapper messageWrapper = getMessageWrapper(
                wrapperByteString);

        // try to process the UnpackedMessage with the MessageWrapper type but message object
        messageProcessingService.processIncomingMessage(messageWrapper);

        // verify that the addMessage or messageReceived method are never called.
        verify(messageStore, times(0)).addMessage(any(Message.class));
    }

    private HanRoutingProtocol.MessageWrapper getMessageWrapper(final ByteString message) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper
                .newBuilder();
        messageWrapperBuilder.setPort(1111);
        messageWrapperBuilder.setIPaddress("127.0.0.1");
        messageWrapperBuilder.setUsername("Alice");

        // encrypt it with our public key, thus only we can decrypt it (must be same instance, see initSerivce).
        ByteString encryptedData = ByteString.copyFrom(encryptionService
                .encryptData(encryptionService.getPublicKey(), message.toByteArray()));

        messageWrapperBuilder.setData(encryptedData);

        return messageWrapperBuilder.build();
    }

    private HanRoutingProtocol.Message getMessage() {
        HanRoutingProtocol.Message.Builder messageBuilder = HanRoutingProtocol.Message
                .newBuilder();
        messageBuilder.setId("3333333");
        messageBuilder.setSender("Charlie");
        messageBuilder.setText("Hey Bob!");
        messageBuilder.setTimeSent(33333333);

        return messageBuilder.build();
    }
}
