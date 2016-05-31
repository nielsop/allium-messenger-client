package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.IMessageConfirmation;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class CommonClientGatewayTest {

    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService login;
    private ISendMessage sendMessage;
    private IMessageBuilder messageBuilder;
    private IMessageConfirmation messageConfirmation;

    private byte[] emptyPublicKey = "".getBytes();
    private byte[] privateKey = "".getBytes();
    private String secretHash = "";
    private String testContact = "testUserName";
    private CurrentUser user1 = new CurrentUser("FirstUser", privateKey, secretHash);
    private CurrentUser user2 = new CurrentUser("SecondUser", privateKey, secretHash);

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new CommonClientModule(), new AbstractModule() {
            @Override
            protected void configure() {
                Properties properties = new Properties();

                properties.setProperty("master-server-host", "localhost");
                properties.setProperty("master-server-port", "1024");

                bind(Properties.class).toInstance(properties);
            }

        });

        contactStore = injector.getInstance(IContactStore.class);
        messageStore = injector.getInstance(IMessageStore.class);
        registration = injector.getInstance(IRegistration.class);
        login = injector.getInstance(ILoginService.class);
        sendMessage = injector.getInstance(ISendMessage.class);
        messageBuilder = injector.getInstance(IMessageBuilder.class);
        messageConfirmation = injector.getInstance(IMessageConfirmation.class);

        commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, login, sendMessage,
                messageBuilder, messageConfirmation);
    }

    @Test
    public void testGetCurrentUserAfterSettingNewUserInContactStore() {
        contactStore.setCurrentUser(user1);
        Assert.assertEquals(user1, commonClientGateway.getCurrentUser());
        contactStore.setCurrentUser(user2);
        Assert.assertEquals(user2, commonClientGateway.getCurrentUser());
    }

    @Test
    public void testGetContactsActuallyGetsAllContacts() {
        Assert.assertEquals(contactStore.getAllContacts(), commonClientGateway.getContacts());
        Assert.assertNull(commonClientGateway.findContact(testContact));
        commonClientGateway.addContact(testContact);
        Assert.assertEquals(contactStore.getAllContacts(), commonClientGateway.getContacts());
        Assert.assertNotNull(commonClientGateway.findContact(testContact));
    }

    @Test
    public void removeContactActuallyRemovesContactFromContactStore() {
        Assert.assertNull(commonClientGateway.findContact(testContact));
        commonClientGateway.addContact(testContact);
        Assert.assertNotNull(commonClientGateway.findContact(testContact));
        commonClientGateway.removeContact(testContact);
        Assert.assertNull(commonClientGateway.findContact(testContact));
    }

    @Test
    public void addContactActuallyAddsContactIntoContactStore() {
        Assert.assertNull(commonClientGateway.findContact(testContact));
        commonClientGateway.addContact(testContact);
        Assert.assertNotNull(commonClientGateway.findContact(testContact));
    }
}
