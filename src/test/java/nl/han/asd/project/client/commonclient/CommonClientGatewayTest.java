package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

@Ignore
public class CommonClientGatewayTest {

    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService login;

    private String emptyPublicKey = "";
    private String privateKey = "";
    private String secretHash = "";
    private Contact contact1 = new Contact("FirstUser", emptyPublicKey.getBytes());
    private CurrentUser user1 = new CurrentUser("FirstUser", privateKey.getBytes(), secretHash);
    private Contact contact2 = new Contact("SecondUser", emptyPublicKey.getBytes());
    private CurrentUser user2 = new CurrentUser("SecondUser", privateKey.getBytes(), secretHash);

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


        //commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, login);

        //TODO: Update those tests; Jasper says this is not how inject should work, but if we delete the injectors, the tests will fail.

        commonClientGateway = injector.getInstance(CommonClientGateway.class);

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
