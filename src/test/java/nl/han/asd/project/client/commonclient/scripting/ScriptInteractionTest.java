package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

public class ScriptInteractionTest
{
    private ScriptInteraction scriptInteraction;
    IContactStore contactStore;
    IMessageStore messageStore;
    ISendMessage sendMessage;

    @Before
    public void setup(){
        contactStore = Mockito.mock(IContactStore.class);
        messageStore = Mockito.mock(IMessageStore.class);
        sendMessage = Mockito.mock(ISendMessage.class);
        scriptInteraction = new ScriptInteraction(contactStore, messageStore, sendMessage);
    }

    @Test
    public void sendMessageTest()
    {
        assertTrue(scriptInteraction.sendMessage("username", "message"));
        Mockito.verify(contactStore, Mockito.times(1)).findContact(any(String.class));
        Mockito.verify(contactStore, Mockito.times(1)).getCurrentUserAsContact();
        Mockito.verify(sendMessage, Mockito.times(1)).sendMessage(any(Message.class), any(Contact.class));
    }

    @Test
    public void sendMessageWithNullValueTest()
    {
        scriptInteraction = new ScriptInteraction(contactStore, messageStore, null);
        assertFalse(scriptInteraction.sendMessage("username", "message"));
    }

    @Test
    public void getReceivedMessagesTest()
    {
        Message message = new Message(new Contact("username"), new Contact("username2"), new Timestamp(1), "text");
        Mockito.when(messageStore.getMessagesAfterDate(any(long.class))).thenReturn(new Message[]{message});
        scriptInteraction.getReceivedMessages(new Date());
        Mockito.verify(messageStore, Mockito.times(1)).getMessagesAfterDate(any(long.class));
    }

    @Test
    public void printUITest()
    {

    }


}
