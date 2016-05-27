package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.cryptography.EncryptionService;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetGraphUpdates;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.path.IGetMessagePath;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
    IGetGraphUpdates updatedGraphMock;
    @Mock
    IEncryptionService encrypt = Mockito.mock(EncryptionService.class);
    @Mock
    ISendMessage sendMessage;
    @Mock
    IMessageStore messageStore;
    @Mock
    IGetMessagePath getPath;
<<<<<<< HEAD

    @Mock
    IContactStore contactStore;

=======
    @Mock
    IContactStore contactStore;
    IGetMessagePath pathDeterminationService = Mockito.mock(PathDeterminationService.class);
>>>>>>> hotfix/sonar-issues
    @InjectMocks
    private MessageBuilderService messageBuilderService;

    @Before
    public void setUp() throws Exception {
<<<<<<< HEAD
        messageBuilderService = new MessageBuilderService(getPath,encrypt, contactStore);
    }

    @Test
    public void sendMessageTest(){
        //TODO: fix this test that takes ridiculously long to execute and doesn't even test anything
        Contact contactReciever = new Contact("julius","1234".getBytes());
        Contact contactSender = new Contact("bram","123456".getBytes());
        contactReciever.setConnectedNodes(new Node[]{new Node("NODE_ID_1","192.168.2.8",1234,"123456789".getBytes()),new Node("NODE_ID_2","192.168.2.9",1234,"123456789".getBytes()),new Node("NODE_ID_3","192.168.2.10",1234,"123456789".getBytes())});
        ArrayList<Node> path = new ArrayList<Node>(Arrays.asList(new Node("NODE_ID_1","192.168.2.1",1234,"123456789".getBytes()), new Node("NODE_ID_2","192.168.2.2",1234,"123456789".getBytes()), new Node("NODE_ID_3","192.168.2.3",1234,"123456789".getBytes())));
        Byte[] encryptedData = new Byte[]{0,1,0,1};
        Mockito.when(pathDeterminationService.getPath(anyInt(),any(Contact.class))).thenReturn(path);
=======
        messageBuilderService = new MessageBuilderService(getPath, encrypt, contactStore);
    }

    @Test
    @Ignore("Needs to be fixed, takes too long to execute.")
    public void sendMessageTest() {
        Contact contactReciever = new Contact("julius", "1234".getBytes());
        Contact contactSender = new Contact("bram", "123456".getBytes());
        contactReciever.setConnectedNodes(
                new Node[] { new Node("NODE_ID_1", "192.168.2.8", 1234, "123456789".getBytes()), new Node("NODE_ID_2", "192.168.2.9", 1234, "123456789".getBytes()),
                        new Node("NODE_ID_3", "192.168.2.10", 1234, "123456789".getBytes()) });
        ArrayList<Node> path = new ArrayList<Node>(
                Arrays.asList(new Node("NODE_ID_1", "192.168.2.1", 1234, "123456789".getBytes()), new Node("NODE_ID_2", "192.168.2.2", 1234, "123456789".getBytes()),
                        new Node("NODE_ID_3", "192.168.2.3", 1234, "123456789".getBytes())));
        Byte[] encryptedData = new Byte[] { 0, 1, 0, 1 };
        Mockito.when(pathDeterminationService.getPath(anyInt(), any(Contact.class))).thenReturn(path);
>>>>>>> hotfix/sonar-issues
        //Mockito.when(encrypt.encryptData(Mockito.any(ByteString.class),Mockito.any(byte[].class))).thenReturn(ByteString.copyFromUtf8("data"));
        Mockito.when(pathDeterminationService.getPath(anyInt(), any(Contact.class))).thenReturn(path);
        //        messageBuilderService.sendMessage("hallo 124",contactReciever,contactSender);
    }

    @After
    public void tearDown() throws Exception {

    }
}
