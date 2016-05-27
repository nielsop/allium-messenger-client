package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetGraphUpdates;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * @author Julius
 * @version 2.0
 * @since 15/04/1996
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageBuilderServiceTest {

    @Mock
    private IGetGraphUpdates updatedGraphMock;

    @Mock
    private ISendMessage sendMessage;

    @Mock
    private IMessageStore messageStore;

    @Mock
    private IGetMessagePath pathDeterminationService;

    @Mock
    private IEncryptionService encrypt;

    private MessageBuilderService messageBuilderService;

    private UUID MESSAGEID;
    private String CONTACTRECIEVERUSERNAME = "Bob";
    private Contact CONTACTRECIEVER;
    private ArrayList<Node> PATH;


    @Before
    public void setUp() throws Exception {
        messageBuilderService = new MessageBuilderService(pathDeterminationService,encrypt);

        MESSAGEID = UUID.randomUUID();

        CONTACTRECIEVER = Mockito.mock(Contact.class);

        PATH = new ArrayList<>(Arrays.asList(new Node("NODE_ID_1","192.168.2.1",1234,"123456789".getBytes()), new Node("NODE_ID_2","192.168.2.2",1234,"123456789".getBytes()), new Node("NODE_ID_3","192.168.2.3",1234,"123456789".getBytes())));

        when(CONTACTRECIEVER.getUsername()).thenReturn(CONTACTRECIEVERUSERNAME);

        when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(PATH);
        when(encrypt.encryptData(any(),any())).thenReturn("encrypted data".getBytes());
    }

    @Test
    public void buildMessageTest(){

        GeneratedMessage message = makeMessage();
        List<Node> path = new ArrayList<>(PATH.size());
        path.addAll(PATH);

        HanRoutingProtocol.MessageWrapper buildMessage = messageBuilderService.buildMessage(message,CONTACTRECIEVER);

        HanRoutingProtocol.Wrapper.Builder wrapperBuilder = HanRoutingProtocol.Wrapper.newBuilder();
        wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGE);
        
        wrapperBuilder.setData(message.toByteString());

        HanRoutingProtocol.MessageWrapper.Builder endPointMessageWrapper = HanRoutingProtocol.MessageWrapper.newBuilder();
        endPointMessageWrapper.setIPaddress(path.get(0).getIpAddress());
        endPointMessageWrapper.setPort(path.get(0).getPort());
        endPointMessageWrapper.setUsername(CONTACTRECIEVER.getUsername());
        endPointMessageWrapper.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(0).getPublicKey(),wrapperBuilder.build().toByteArray()))); // enc

        for(int i = 1; i < path.size(); i++){
            HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

            messageWrapperBuilder.setIPaddress(path.get(i).getIpAddress());
            messageWrapperBuilder.setPort(path.get(i).getPort());
            messageWrapperBuilder.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(i).getPublicKey(),endPointMessageWrapper.build().toByteArray()))); // enc

            endPointMessageWrapper = messageWrapperBuilder;

        }
        Assert.assertEquals(buildMessage,endPointMessageWrapper.build());
    }

    @Test
    public void buildMessage2hops(){
        PATH = new ArrayList<>(Arrays.asList(new Node("NODE_ID_1-last-node-in-path","192.168.2.1",1234,"123456789".getBytes()), new Node("NODE_ID_2","192.168.2.2",1234,"123456789".getBytes())));

        List<Node> path = new ArrayList<>(PATH.size());
        path.addAll(PATH);
        pathDeterminationService = Mockito.mock(IGetMessagePath.class);

        messageBuilderService = new MessageBuilderService(pathDeterminationService,encrypt);

        when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(PATH);

        GeneratedMessage message = makeMessage();

        HanRoutingProtocol.MessageWrapper buildMessage = messageBuilderService.buildMessage(message,CONTACTRECIEVER);

        HanRoutingProtocol.Wrapper.Builder wrapperBuilder = HanRoutingProtocol.Wrapper.newBuilder();
        wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGE);

        wrapperBuilder.setData(message.toByteString());

        HanRoutingProtocol.MessageWrapper.Builder endPointMessageWrapper = HanRoutingProtocol.MessageWrapper.newBuilder();
        endPointMessageWrapper.setIPaddress(path.get(0).getIpAddress());
        endPointMessageWrapper.setPort(path.get(0).getPort());
        endPointMessageWrapper.setUsername(CONTACTRECIEVER.getUsername());
        endPointMessageWrapper.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(0).getPublicKey(),wrapperBuilder.build().toByteArray())));

        HanRoutingProtocol.MessageWrapper.Builder secondDeepestLayer = HanRoutingProtocol.MessageWrapper.newBuilder();

        secondDeepestLayer.setIPaddress(path.get(1).getIpAddress());
        secondDeepestLayer.setPort(path.get(1).getPort());
        secondDeepestLayer.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(1).getPublicKey(),endPointMessageWrapper.build().toByteArray())));

        Assert.assertEquals(buildMessage,secondDeepestLayer.build());

    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void whenThereIsNoNodesInPathThrowIndexOutOfBoundsException(){
        PATH = new ArrayList<>();

        pathDeterminationService = Mockito.mock(IGetMessagePath.class);

        messageBuilderService = new MessageBuilderService(pathDeterminationService,encrypt);

        when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(PATH);
        GeneratedMessage message = makeMessage();

        messageBuilderService.buildMessage(message,CONTACTRECIEVER);

    }

    @Test
    public void buildConfirmationMessageTest(){
        PATH = new ArrayList<>(Arrays.asList(new Node("NODE_ID_1-last-node-in-path","192.168.2.1",1234,"123456789".getBytes()), new Node("NODE_ID_2","192.168.2.2",1234,"123456789".getBytes())));

        List<Node> path = new ArrayList<>(PATH.size());
        path.addAll(PATH);

        pathDeterminationService = Mockito.mock(IGetMessagePath.class);

        messageBuilderService = new MessageBuilderService(pathDeterminationService,encrypt);

        when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(PATH);

        GeneratedMessage messageConfirmation = makeMessageConfirmation();

        HanRoutingProtocol.MessageWrapper buildMessage = messageBuilderService.buildMessage(messageConfirmation,CONTACTRECIEVER);

        HanRoutingProtocol.Wrapper.Builder wrapperBuilder = HanRoutingProtocol.Wrapper.newBuilder();
        wrapperBuilder.setType(HanRoutingProtocol.Wrapper.Type.MESSAGECONFIRMATION);

        wrapperBuilder.setData(messageConfirmation.toByteString());

        HanRoutingProtocol.MessageWrapper.Builder endPointMessageWrapper = HanRoutingProtocol.MessageWrapper.newBuilder();
        endPointMessageWrapper.setIPaddress(path.get(0).getIpAddress());
        endPointMessageWrapper.setPort(path.get(0).getPort());
        endPointMessageWrapper.setUsername(CONTACTRECIEVER.getUsername());
        endPointMessageWrapper.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(0).getPublicKey(),wrapperBuilder.build().toByteArray())));

        HanRoutingProtocol.MessageWrapper.Builder secondDeepestLayer = HanRoutingProtocol.MessageWrapper.newBuilder();

        secondDeepestLayer.setIPaddress(path.get(1).getIpAddress());
        secondDeepestLayer.setPort(path.get(1).getPort());
        secondDeepestLayer.setEncryptedData(ByteString.copyFrom(encrypt.encryptData(path.get(1).getPublicKey(),endPointMessageWrapper.build().toByteArray())));

        HanRoutingProtocol.MessageWrapper secondDeepestLayerBuild = secondDeepestLayer.build();
        Assert.assertEquals(buildMessage,secondDeepestLayerBuild);

    }

    @Test (expected = IllegalArgumentException.class)
    public void throwExeptionWhenWrongMessageIsUsed(){
        messageBuilderService.buildMessage(makeWrongMessage(),CONTACTRECIEVER);
    }

    private HanRoutingProtocol.Message makeMessage(){
        HanRoutingProtocol.Message.Builder messageBuilder = HanRoutingProtocol.Message.newBuilder();

        messageBuilder.setText("This is a message");
        messageBuilder.setSender("Alice");
        messageBuilder.setTimeSent(Instant.now().getEpochSecond());
        messageBuilder.setId(MESSAGEID.toString());

        return messageBuilder.build();
    }

    private HanRoutingProtocol.MessageConfirmation makeMessageConfirmation(){
        HanRoutingProtocol.MessageConfirmation.Builder messageConfirmation = HanRoutingProtocol.MessageConfirmation.newBuilder();

        messageConfirmation.setConfirmationId(MESSAGEID.toString());

        return messageConfirmation.build();
    }

    private HanRoutingProtocol.MessageWrapper makeWrongMessage(){
        HanRoutingProtocol.MessageWrapper.Builder messageWrapperBuilder = HanRoutingProtocol.MessageWrapper.newBuilder();

        messageWrapperBuilder.setEncryptedData(ByteString.copyFrom("data".getBytes()));
        messageWrapperBuilder.setPort(1337);
        messageWrapperBuilder.setIPaddress("192.168.2.14");

        return messageWrapperBuilder.build();
    }

    @After
    public void tearDown() throws Exception {

    }
}