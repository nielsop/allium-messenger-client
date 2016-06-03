package nl.han.asd.project.client.commonclient;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import com.xebialabs.overcast.host.DockerHost;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.graph.GraphManagerService;
import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.ISubscribeMessageReceiver;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Raoul on 31/5/2016.
 */
@Ignore
public class FullIntegrationTest {

    private String masterHost;
    private int masterPort;
    private int nextPort;

    @Before
    public void setup() {
        CloudHost master = CloudHostFactory.getCloudHost("master");
        master.setup();
        masterHost = master.getHostName();
        masterPort = master.getPort(1337);

        nextPort = masterPort + 1;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3; i++) {
            startNode();
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ok() {
        System.out.println("gelukt!");

        final Properties properties = new Properties();
        properties.setProperty("master-server-host", masterHost);
        properties.setProperty("master-server-port", Integer.toString(masterPort));

        Injector injector = Guice.createInjector(new CommonClientModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Properties.class).toInstance(properties);
                    }
                });
//        MasterGateway gateway = (MasterGateway) injector.getInstance(IAuthentication.class);

        GraphManagerService graphManagerService = (GraphManagerService) injector.getInstance(IGetVertices.class);

        IContactStore contactStore = injector.getInstance(IContactStore.class);
        IMessageStore messageStore = injector.getInstance(IMessageStore.class);
        IRegistration registration = injector.getInstance(IRegistration.class);
        ILoginService loginService = injector.getInstance(ILoginService.class);
        ISendMessage sendMessage = injector.getInstance(ISendMessage.class);
        ISubscribeMessageReceiver subscribeMessageReceiver = injector.getInstance(ISubscribeMessageReceiver.class);

        CommonClientGateway commonClientGateway = new CommonClientGateway(contactStore, messageStore, registration, loginService, sendMessage, subscribeMessageReceiver);
        try {
            commonClientGateway.registerRequest("raoul", "test1234", "test1234");
            commonClientGateway.loginRequest("raoul", "test1234");

            Message message = new Message(contactStore.getCurrentUserAsContact(), contactStore.getCurrentUserAsContact(), new Date(), "TEST BOODSCHAP", "MessageId1");
            commonClientGateway.sendMessage(message);
        } catch (IOException | InvalidCredentialsException | MessageNotSentException e) {
            e.printStackTrace();
        }
    }

    private void startNode() {
        List<String> nodeCommand = new ArrayList<>();
        nodeCommand.add("masterIP=" + masterHost);
        nodeCommand.add("masterPort=" + masterPort);
        nodeCommand.add("nodePort=" + nextPort);
        DockerHost node = (DockerHost) CloudHostFactory.getCloudHost("node");
        node.setEnv(nodeCommand);
        node.setup();

        nextPort++;
    }
}
