package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.path.PathDeterminationService;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Created by Julius on 15/04/16.
 */
public class MessageBuilderServiceTest {

    private MessageBuilderService messageBuilderService;
    private CryptographyService cryptographyService;
    @Mock
    IGetUpdatedGraph updatedGraphMock;

    @Mock
    IGetClientGroup clientGroupMock;

    @InjectMocks
    private PathDeterminationService pathDeterminationService;

    @Before
    public void setUp() throws Exception {
       // pathDeterminationService = new PathDeterminationService();
     //   messageBuilderService = new MessageBuilderService(pathDeterminationService);
    }

    @Test
    public void buildMessage(){
        //cryptographyService = new CryptographyService();
        messageBuilderService = new MessageBuilderService(pathDeterminationService,cryptographyService);
    }

    @Test
    public void sendMessageTest(){
        Contact contactReciever = new Contact("julius","1234");
        Contact contactSender = new Contact("bram","123456");
        contactSender.setConnectedNodes(new Node[]{new Node(),new Node(),new Node()});
        messageBuilderService.sendMessage("hallo 124",contactReciever,contactSender);
    }


    @After
    public void tearDown() throws Exception {

    }
}