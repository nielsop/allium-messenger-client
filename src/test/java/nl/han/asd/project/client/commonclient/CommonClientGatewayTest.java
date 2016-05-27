package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class CommonClientGatewayTest {

    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageStoreObserver messageStoreObserver;
    private IRegistration registration;
    private ILogin login;

    private String emptyPublicKey = "";
    private String privateKey = "";
    private String secretHash = "";
    private Contact contact1 = new Contact("FirstUser", emptyPublicKey.getBytes());
    private CurrentUser user1 = new CurrentUser("FirstUser", privateKey.getBytes(), secretHash);
    private Contact contact2 = new Contact("SecondUser", emptyPublicKey.getBytes());
    private CurrentUser user2 = new CurrentUser("SecondUser", privateKey.getBytes(), secretHash);

    @Before
    public void setup() {

        Injector injector = Guice.createInjector(new CommonclientModule());
        contactStore = injector.getInstance(IContactStore.class);
        messageStore = injector.getInstance(IMessageStore.class);
        messageBuilder = injector.getInstance(IMessageBuilder.class);
        messageStoreObserver = injector.getInstance(IMessageStoreObserver.class);
        registration = injector.getInstance(IRegistration.class);
        login = injector.getInstance(ILogin.class);

        commonClientGateway = new CommonClientGateway(contactStore, messageStore, messageBuilder, messageStoreObserver, registration, login);
    }


    @Test
    public void testGetCurrentUserAfterSettingNewUserInContactStore() {
        contactStore.setCurrentUser(user1);
        Assert.assertTrue(commonClientGateway.getCurrentUser() == user1);
        contactStore.setCurrentUser(user2);
        Assert.assertTrue(commonClientGateway.getCurrentUser() == user2);
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
    public void removeContactActuallyRemovesContactFromContactStore() {
        String newContact = "newContact";
        Assert.assertTrue(contactStore.findContact(newContact) == null);
        contactStore.addContact(newContact, emptyPublicKey.getBytes());
        Assert.assertTrue(contactStore.findContact(newContact).getUsername() == newContact);
        commonClientGateway.removeContact(newContact);
        Assert.assertTrue(contactStore.findContact(newContact) == null);

    }

}
