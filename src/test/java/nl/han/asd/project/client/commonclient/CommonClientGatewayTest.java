package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.graph.IUpdateGraph;
import nl.han.asd.project.client.commonclient.heartbeat.IHeartbeatService;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.master.IRegistration;

import nl.han.asd.project.client.commonclient.message.*;
import nl.han.asd.project.client.commonclient.store.*;

import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.ISubscribeMessageReceiver;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.IScriptStore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

public class CommonClientGatewayTest {

    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService login;
    private ISendMessage sendMessage;
    private IScriptStore scriptStore;
    private IHeartbeatService heartbeatService;

    private ISubscribeMessageReceiver subscribeMessageReceiver;
    private byte[] privateKey = "".getBytes();
    private String secretHash = "";
    private String testContact = "testUserName";
    private CurrentUser user1 = new CurrentUser("FirstUser", privateKey, secretHash);
    private CurrentUser user2 = new CurrentUser("SecondUser", privateKey, secretHash);
    private IMessageConfirmation messageConfirmation;
    private IUpdateGraph updateGraph;

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
        scriptStore = injector.getInstance(IScriptStore.class);
        subscribeMessageReceiver = injector.getInstance(ISubscribeMessageReceiver.class);
        heartbeatService = injector.getInstance(IHeartbeatService.class);
        messageConfirmation = injector.getInstance(IMessageConfirmation.class);
        updateGraph = injector.getInstance(IUpdateGraph.class);
        commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, login, scriptStore,
                sendMessage,subscribeMessageReceiver, heartbeatService, messageConfirmation, updateGraph);
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

    @Test
    public void sendMessageTest()
    {
        CurrentUser currentUser = Mockito.mock(CurrentUser.class);
        contactStore = Mockito.mock(IContactStore.class);
        when(contactStore.findContact(any(String.class))).thenReturn(new Contact("iemand"));
        when(contactStore.getCurrentUser()).thenReturn(currentUser);
        sendMessage = Mockito.mock(ISendMessage.class);
        commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, login, scriptStore, sendMessage,
                subscribeMessageReceiver, heartbeatService, messageConfirmation, updateGraph);
        assertTrue(commonClientGateway.sendMessage("username", "message"));
        Mockito.verify(contactStore, Mockito.times(1)).findContact(any(String.class));
        Mockito.verify(contactStore, Mockito.times(1)).getCurrentUser();
        Mockito.verify(sendMessage, Mockito.times(1)).sendMessage(any(Message.class), any(Contact.class));
    }

    @Test
    public void sendMessageWithNullValueTest()
    {
        assertFalse(commonClientGateway.sendMessage("username", "message"));
    }

    @Test
    public void getReceivedMessagesTest()
    {
        Message message = new Message(new Contact("username"), new Contact("username2"), new Timestamp(1), "text");
        messageStore = Mockito.mock(IMessageStore.class);
        Mockito.when(messageStore.getMessagesAfterDate(any(long.class))).thenReturn(new Message[]{message});
        commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, login, scriptStore,
                sendMessage, subscribeMessageReceiver, heartbeatService, messageConfirmation, updateGraph);
        commonClientGateway.getReceivedMessageAfterDate(new Date());
        Mockito.verify(messageStore, Mockito.times(1)).getMessagesAfterDate(any(long.class));
    }
}
