package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommonClientGatewayTest {

    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageObserver messageObserver;
    private IRegistration registration;
    private ILogin login;

    private String emptyPublicKey = "";
    private Contact contact1 = new Contact("FirstUser", emptyPublicKey);
    private Contact contact2 = new Contact("SecondUser", emptyPublicKey);

    @Before
    public void setup() {

        Injector injector = Guice.createInjector(new CommonclientModule());
        contactStore = injector.getInstance(IContactStore.class);
        messageStore = injector.getInstance(IMessageStore.class);
        messageBuilder = injector.getInstance(IMessageBuilder.class);
        messageObserver = injector.getInstance(IMessageObserver.class);
        registration = injector.getInstance(IRegistration.class);
        login = injector.getInstance(ILogin.class);

        commonClientGateway = new CommonClientGateway(contactStore, messageStore, messageBuilder, messageObserver, registration, login);
    }

    @Test
    public void testGetMessagesFromUserActuallyGetsAllMessagesFromUser() {
        Assert.assertTrue(commonClientGateway.getMessagesFromUser(contact1.getUsername()).isEmpty());
        Message message1 = new Message("Message 1", contact1, contact2, System.currentTimeMillis());
        Message message2 = new Message("Message 2", contact1, contact2, System.currentTimeMillis());
        Assert.assertFalse(commonClientGateway.getMessagesFromUser(contact1.getUsername()).contains(message1));
        Assert.assertFalse(commonClientGateway.getMessagesFromUser(contact1.getUsername()).contains(message2));
        commonClientGateway.addMessage(message1);
        commonClientGateway.addMessage(message2);
        Assert.assertTrue(commonClientGateway.getMessagesFromUser(contact1.getUsername()).size() == 2);
        Assert.assertTrue(commonClientGateway.getMessagesFromUser(contact1.getUsername()).contains(message1));
        Assert.assertTrue(commonClientGateway.getMessagesFromUser(contact1.getUsername()).contains(message2));
    }

    @Test
    public void testGetCurrentUserAfterSettingNewUserInContactStore() {
        contactStore.setCurrentUser(contact1);
        Assert.assertTrue(commonClientGateway.getCurrentUser() == contact1);
        contactStore.setCurrentUser(contact2);
        Assert.assertTrue(commonClientGateway.getCurrentUser() == contact2);
    }

    @Test
    public void testGetContactsActuallyGetsAllContacts() {
        Assert.assertTrue(contactStore.getAllContacts() == commonClientGateway.getContacts());
        Assert.assertFalse(commonClientGateway.getContacts().contains(contact1));
        commonClientGateway.getContacts().add(contact1);
        Assert.assertTrue(contactStore.getAllContacts() == commonClientGateway.getContacts());
        Assert.assertTrue(commonClientGateway.getContacts().contains(contact1));
    }

    @Test
    public void testSendMessageAddsMessageToMessageStore() {
        int sizeBefore = messageStore.getMessagesFromUser(contact1.getUsername()).size();
        commonClientGateway.sendMessage(new Message("newMessage", contact1, contact2, System.currentTimeMillis()));
        Assert.assertTrue(sizeBefore + 1 == messageStore.getMessagesFromUser(contact1.getUsername()).size());
    }

    @Test
    public void removeContactActuallyRemovesContactFromContactStore() {
        String newContact = "newContact";
        Assert.assertTrue(contactStore.findContact(newContact) == null);
        contactStore.addContact(newContact, emptyPublicKey);
        Assert.assertTrue(contactStore.findContact(newContact).getUsername() == newContact);
        commonClientGateway.removeContact(newContact);
        Assert.assertTrue(contactStore.findContact(newContact) == null);

    }

}
