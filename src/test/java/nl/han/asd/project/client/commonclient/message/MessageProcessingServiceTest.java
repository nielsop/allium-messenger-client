package nl.han.asd.project.client.commonclient.message;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.store.MessageStore;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Jevgeni on 17-5-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageProcessingServiceTest {

    @Mock private MessageStore messageStore;

    private CryptographyService cryptographyService;

    private MessageProcessingService messageProcessingService;

    @Before public void initService() {
        final Injector injector = Guice.createInjector(new EncryptionModule());

        cryptographyService = new CryptographyService(injector.getInstance(IEncryptionService.class));

        messageProcessingService = new MessageProcessingService(messageStore,
                cryptographyService);
    }

    @Test public void testWithMessageWrapper() {
        HanRoutingProtocol.Message message = getMessage();

        ByteString wrapperByteString = HanRoutingProtocol.Wrapper.newBuilder().setData(message.toByteString())
                .setType(HanRoutingProtocol.Wrapper.Type.MESSAGE).build().toByteString();

        HanRoutingProtocol.MessageWrapper messageWrapper = getMessageWrapper(
                wrapperByteString);
        messageProcessingService.processMessage(messageWrapper);

        // verify that the addMessage is called (thus no exceptions where thrown)
        verify(messageStore, times(1)).addMessage(eq(message), eq(messageWrapper.getUsername()));
    }


    @Test public void testWithMessageConfirmationWrapper() {
        HanRoutingProtocol.MessageConfirmation messageConfirmation = HanRoutingProtocol.MessageConfirmation.newBuilder().setConfirmationId("1111111").build();

        ByteString wrapperByteString = HanRoutingProtocol.Wrapper.newBuilder().setData(messageConfirmation.toByteString())
                .setType(HanRoutingProtocol.Wrapper.Type.MESSAGECONFIRMATION).build().toByteString();

        HanRoutingProtocol.MessageWrapper messageWrapper = getMessageWrapper(
                wrapperByteString);
        messageProcessingService.processMessage(messageWrapper);

        // verify that the messageReceived is called (thus no exceptions where thrown)
        verify(messageStore, times(1)).messageReceived(eq(messageConfirmation.getConfirmationId()));
    }

    @Test public void testInvalidType() {
        HanRoutingProtocol.GraphUpdateRequest.Builder graphUpdateRequestBuilder = HanRoutingProtocol.GraphUpdateRequest
                .newBuilder();
        graphUpdateRequestBuilder.setCurrentVersion(1);

        // build wrapper containing Type.GRAPHUPDATEREQUEST, processMessage should be unable to parse this.
        ByteString wrapperByteString = HanRoutingProtocol.Wrapper.newBuilder()
                .setData(graphUpdateRequestBuilder.build().toByteString()).setType(HanRoutingProtocol.Wrapper.Type.GRAPHUPDATEREQUEST)
                .build().toByteString();

        HanRoutingProtocol.MessageWrapper messageWrapper = getMessageWrapper(
                wrapperByteString);
        messageProcessingService.processMessage(messageWrapper);

        // verify that the addMessage or messageReceived method are never called.
        verify(messageStore, times(0)).addMessage(any(), any());
        verify(messageStore, times(0)).messageReceived(any());
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
        messageProcessingService.processMessage(messageWrapper);

        // verify that the addMessage or messageReceived method are never called.
        verify(messageStore, times(0)).addMessage(any(), any());
        verify(messageStore, times(0)).messageReceived(any());
    }

    private HanRoutingProtocol.MessageWrapper getMessageWrapper(final ByteString message) {
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper
                .newBuilder();
        messageWrapperBuilder.setPort(1111);
        messageWrapperBuilder.setIPaddress("127.0.0.1");
        messageWrapperBuilder.setUsername("Alice");

        // encrypt it with our public key, thus only we can decrypt it (must be same instance, see initSerivce).
        ByteString encryptedData = cryptographyService
                .encryptData(message, cryptographyService.getPublicKey());

        messageWrapperBuilder.setEncryptedData(encryptedData);

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
