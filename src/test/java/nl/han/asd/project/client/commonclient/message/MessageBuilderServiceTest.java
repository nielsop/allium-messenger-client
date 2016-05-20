package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;

/**
 * Created by Julius on 15/04/16.
 */
public class MessageBuilderServiceTest {

    @Mock
    IGetUpdatedGraph updatedGraphMock;
    @Mock
    IEncrypt encrypt = Mockito.mock(CryptographyService.class);
    @Mock
    ISendMessage sendMessage;
    @Mock
    IMessageStore messageStore;

    @InjectMocks
    private MessageBuilderService messageBuilderService;

    IGetPath pathDeterminationService = Mockito
            .mock(PathDeterminationService.class);

    @Before
    public void setUp() throws Exception {
        messageBuilderService = new MessageBuilderService(pathDeterminationService,encrypt,sendMessage,messageStore);
    }


    //TODO: SetConnectedNodes has been removed.
//    @Test
//    public void sendMessageTest(){
//        Contact contactReciever = new Contact("julius","1234");
//        Contact contactSender = new Contact("bram","123456");
//        contactReciever.setConnectedNodes(new Node[]{new Node("NODE_ID_1","192.168.2.8",1234,"123456789".getBytes()),new Node("NODE_ID_2","192.168.2.9",1234,"123456789".getBytes()),new Node("NODE_ID_3","192.168.2.10",1234,"123456789".getBytes())});
//
//        ArrayList<Node> path = new ArrayList<Node>(Arrays.asList(new Node("NODE_ID_1","192.168.2.1",1234,"123456789".getBytes()), new Node("NODE_ID_2","192.168.2.2",1234,"123456789".getBytes()), new Node("NODE_ID_3","192.168.2.3",1234,"123456789".getBytes())));
//        Byte[] encryptedData = new Byte[]{0,1,0,1};
//
//        Mockito.when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(path);
//        Mockito.when(encrypt.encryptData(Mockito.any(ByteString.class),Mockito.any(byte[].class))).thenReturn(ByteString.copyFromUtf8("data"));
//        Mockito.when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(path);
//        messageBuilderService.sendMessage("hallo 124",contactReciever,contactSender);
//	}



    @After
    public void tearDown() throws Exception {

    }
}
