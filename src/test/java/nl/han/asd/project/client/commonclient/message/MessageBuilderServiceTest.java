package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.client.commonclient.message.EncryptedMessage;
import nl.han.asd.client.commonclient.message.MessageBuilderService;
import nl.han.asd.client.commonclient.path.PathDeterminationService;
import nl.han.asd.client.commonclient.store.Contact;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Julius on 15/04/16.
 */
public class MessageBuilderServiceTest {

    private MessageBuilderService messageBuilderService;
    private PathDeterminationService pathDeterminationService;
    @Before
    public void setUp() throws Exception {
       // pathDeterminationService = new PathDeterminationService();
     //   messageBuilderService = new MessageBuilderService(pathDeterminationService);
    }

    @Test
    public void buildMessage(){
       // Contact contactOntvanger = new Contact("julius");
       //TODO EncryptedMessage enMessage = messageBuilderService.buildMessage("hallo 124",contactOntvanger);

        //TODOAssert.assertEquals(enMessage.getUsername(),contactOntvanger.getUsername());
    }


    @After
    public void tearDown() throws Exception {

    }
}