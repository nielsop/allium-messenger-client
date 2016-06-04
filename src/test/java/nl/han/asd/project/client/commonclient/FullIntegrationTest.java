package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import com.xebialabs.overcast.host.DockerHost;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.graph.IUpdateGraph;
import nl.han.asd.project.client.commonclient.heartbeat.IHeartbeatService;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.*;
import nl.han.asd.project.client.commonclient.store.*;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Ignore
public class FullIntegrationTest {

    //    private String masterHost = "195.169.194.234";
//    private int masterPort = 33255;
    private String masterHost;
    private int masterPort;
    private int nextPort;

    private CloudHost master;
    private List<DockerHost> nodes = new ArrayList<>();
    private DockerHost client;
    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService loginService;
    private ISendMessage sendMessage;
    private ISubscribeMessageReceiver subscribeMessageReceiver;
    private IScriptStore scriptStore;
    private IHeartbeatService heartbeatService;
    private IUpdateGraph updateGraph;
    private IMessageConfirmation messageConfirmation;

    private CommonClientGateway commonClientGateway;

    private Message receivedMessage;
    private String confirmedMessage;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        receivedMessage = null;
        confirmedMessage = null;
    }

    @After
    public void tearDown() {
        if (master != null) {
            master.teardown();
        }
        if (!nodes.isEmpty()) {
            for (DockerHost node : nodes) {
                System.out.println("Going down");
                node.teardown();
            }
            nodes.clear();
        }
        if (client != null) {
            client.teardown();
        }
    }

    @Test
    public void testRegisterClientWithoutMasterReturnsError() {
        injectInterfaces();
        exception.expect(NullPointerException.class);

        try {
            commonClientGateway.registerRequest("niels", "test1234", "test1234");
        } catch (IOException | MessageNotSentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testRegisterClientReturnsSuccessMessage() {
        startMaster();
        injectInterfaces();

        try {
            Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES,
                    commonClientGateway.registerRequest("OnionTest", "test1234", "test1234"));
        } catch (IOException | MessageNotSentException e) {
            Assert.fail();
        }
    }

    @Test
    public void testLogInExistingClientReturnsSuccessMessage() {
        startMaster();
        injectInterfaces();

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES,
                    commonClientGateway.loginRequest("OnionTest", "test1234"));
            Contact currentUser = contactStore.getCurrentUser().asContact();
            Assert.assertEquals(currentUser.getUsername(), "OnionTest");
            Assert.assertNotNull(contactStore.getCurrentUser().getSecretHash());
            //TODO Find out why currentUser isOnline is false
            //Assert.assertTrue(currentUser.isOnline);
            Assert.assertEquals(currentUser.getConnectedNodes().length, 0);
            Assert.fail();
        } catch (IOException | MessageNotSentException | InvalidCredentialsException e) {
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoConnectedNodesException);
        }
    }

    @Test
    public void testSendMessageToNonExistingContactReturnError() {
        startMaster();
        injectInterfaces();
        exception.expect(NullPointerException.class);

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES,
                    commonClientGateway.loginRequest("OnionTest", "test1234"));
            Contact otherUser = new Contact("OtherUser");
            Message message = new Message(contactStore.getCurrentUser().asContact(),
                    otherUser, new Date(), "TEST Message", "MessageId1");
            commonClientGateway.sendMessage(message);
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSendMessageToExistingContactWithoutExistingNodesReturnError() {
        startMaster();
        injectInterfaces();
        exception.expect(IndexOutOfBoundsException.class);

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            commonClientGateway.loginRequest("OnionTest", "test1234");
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");
            Message message = new Message(contactStore.getCurrentUser().asContact(),
                    otherUser, new Date(), "TEST Message", "MessageId1");
            commonClientGateway.sendMessage(message);
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSendMessageToOfflineContactWithExistingNodesReturnError() {
        startMaster();
        startNodes(4);
        injectInterfaces();
        exception.expect(IndexOutOfBoundsException.class);

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            commonClientGateway.loginRequest("OnionTest", "test1234");
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");
            Message message = new Message(contactStore.getCurrentUser().asContact(),
                    otherUser, new Date(), "TEST Message", "MessageId1");
            commonClientGateway.sendMessage(message);
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSendMessageToOnlineContactMessageDeliveredConfirmationReceived() {
        startMaster();
        startNodes(4);
        startClient("default");
        injectInterfaces();
        observeMessages();

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            commonClientGateway.loginRequest("OnionTest", "test1234");
            heartbeatService.startHeartbeatFor(contactStore.getCurrentUser());
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");
            Message message = new Message(contactStore.getCurrentUser().asContact(),
                    otherUser, new Date(), "TEST Message");
            commonClientGateway.sendMessage(message);

            Thread.sleep(6000);

            Assert.assertTrue(confirmedMessage != null && !confirmedMessage.isEmpty());
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException | InterruptedException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSendMessageToContactComingOnlineLaterDeliveredAfterTimeoutReceiveConfirmation() {
        startMaster();
        startNodes(4);
        injectInterfaces();
        observeMessages();

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            commonClientGateway.loginRequest("OnionTest", "test1234");
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");
            Message message = new Message(contactStore.getCurrentUser().asContact(),
                    otherUser, new Date(), "TEST Message", "MessageId1");
            commonClientGateway.sendMessage(message);

        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException e) {
            Assert.fail();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        try {
            startClient("default");
            Thread.sleep(3000);
            Assert.assertNull(confirmedMessage);
            Thread.sleep(60000);
        } catch (InterruptedException e1) {
            Assert.fail();
        }
        Assert.assertTrue(confirmedMessage != null && !confirmedMessage.isEmpty());
    }

    @Test
    public void testOtherUserSendsMessageWhichIsReceived() {
        startMaster();
        startNodes(3);
        injectInterfaces();
        observeMessages();

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            commonClientGateway.loginRequest("OnionTest", "test1234");
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");

            startClient("send");
            Thread.sleep(10000);

            Assert.assertEquals(receivedMessage.getReceiver(), contactStore.getCurrentUser().asContact());
            Assert.assertEquals(receivedMessage.getSender(), otherUser);
            Assert.assertEquals(receivedMessage.getText(), "Test message");
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException | InterruptedException e) {
            Assert.fail();
        }
    }

    @Test
    public void testOtherUserSendsMessageWhileUserIsOfflineIsReceivedWhenUserComesOnline() {
        startMaster();
        startNodes(3);
        startClient("default");
        injectInterfaces();

        try {
            commonClientGateway.registerRequest("OnionTest", "test1234", "test1234");
            contactStore.addContact("user");
            Contact otherUser = new Contact("user");

            Thread.sleep(3000);
            Assert.assertNull(receivedMessage);
            Thread.sleep(3000);
            commonClientGateway.loginRequest("OnionTest", "test1234");
            Thread.sleep(10000);

            Assert.assertEquals(receivedMessage.getReceiver(), contactStore.getCurrentUser().asContact());
            Assert.assertEquals(receivedMessage.getSender(), otherUser);
            Assert.assertEquals(receivedMessage.getText(), "Test message");
        } catch (IOException | MessageNotSentException | SQLException | InvalidCredentialsException | InterruptedException e) {
            Assert.fail();
        }
    }

    private void startMaster() {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        masterHost = master.getHostName();
        masterPort = master.getPort(1337);

        nextPort = masterPort + 1;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startNodes(int numberNodes) {
        for (int i = 0; i < numberNodes; i++) {
            List<String> nodeCommand = new ArrayList<>();
            nodeCommand.add("masterIP=" + masterHost);
            nodeCommand.add("masterPort=" + masterPort);
            nodeCommand.add("externalPort=" + nextPort);
            DockerHost node = (DockerHost) CloudHostFactory.getCloudHost("node");
            nodes.add(node);
            node.setEnv(nodeCommand);
            node.setup();
            nextPort++;
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startClient(String integrationType) {
        List<String> clientCommand = new ArrayList<>();
        clientCommand.add("master-server-host=" + masterHost);
        clientCommand.add("master-server-port=" + masterPort);
        clientCommand.add("integration-enabled=true");
        clientCommand.add("integration-type=" + integrationType);
        clientCommand.add("heartbeat-delay=3");
        client = (DockerHost) CloudHostFactory.getCloudHost("client");
        client.setEnv(clientCommand);
        client.setup();

        nextPort++;

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void injectInterfaces() {
        final Properties properties = new Properties();
        properties.setProperty("master-server-host", masterHost);
        properties.setProperty("master-server-port", Integer.toString(masterPort));
        properties.setProperty("heartbeat-delay", "3");

        Injector injector = Guice.createInjector(new CommonClientModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Properties.class).toInstance(properties);
                    }
                });

        contactStore = injector.getInstance(IContactStore.class);
        messageStore = injector.getInstance(IMessageStore.class);
        registration = injector.getInstance(IRegistration.class);
        loginService = injector.getInstance(ILoginService.class);
        sendMessage = injector.getInstance(ISendMessage.class);
        subscribeMessageReceiver = injector.getInstance(ISubscribeMessageReceiver.class);
        scriptStore = injector.getInstance(IScriptStore.class);
        heartbeatService = injector.getInstance(IHeartbeatService.class);
        messageConfirmation = injector.getInstance(IMessageConfirmation.class);
        updateGraph = injector.getInstance(IUpdateGraph.class);

        commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, loginService,
                scriptStore, sendMessage, subscribeMessageReceiver, heartbeatService, messageConfirmation, updateGraph);
    }

    private void observeMessages(){
        commonClientGateway.subscribeReceivedMessages(new IMessageReceiver() {
            @Override
            public void receivedMessage(Message message) {
                System.out.println("Received a message");
                receivedMessage = message;
            }

            @Override
            public void confirmedMessage(String messageId) {
                System.out.println("Confirmation received");
                confirmedMessage = messageId;
            }
        });
    }
}
