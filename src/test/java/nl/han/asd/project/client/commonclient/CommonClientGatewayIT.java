package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.*;

import java.io.IOException;
import java.net.Socket;

public class CommonClientGatewayIT {

    private CloudHost master;
    private MasterGateway gateway;
    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageObserver messageObserver;
    private IEncryptionService encryptionService;
    private IRegistration registration;
    private ILogin login;

    private String validUsername = "validUsername";
    private String validPassword = "validPassword";

    @Before
    public void setup(){
        createCloudHost();
        Injector injector = Guice.createInjector(new CommonclientModule());
        contactStore = injector.getInstance(IContactStore.class);
        messageStore = injector.getInstance(IMessageStore.class);
        messageBuilder = injector.getInstance(IMessageBuilder.class);
        messageObserver = injector.getInstance(IMessageObserver.class);
        registration = injector.getInstance(IRegistration.class);
        login = injector.getInstance(ILogin.class);

        commonClientGateway =
                new CommonClientGateway(contactStore, messageStore, messageBuilder,
                        messageObserver, registration, login);
    }

    @After
    public void after(){
        master.teardown();
    }

    @Test @Ignore("Fix?")
    public void testRegisterRequestSuccessful(){
        //ClientRegisterResponse.Status result = ClientRegisterResponse.Status.SUCCES;
        //Assert.assertEquals(result, commonClientGateway.registerRequest(validUsername, validPassword, validPassword));
        commonClientGateway.registerRequest(validUsername, validPassword, validPassword);
    }

    private void createCloudHost() {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        Injector injector = Guice.createInjector(new EncryptionModule());
        while (true) {
            try {
                new Socket(master.getHostName(), master.getPort(1337));
                break;
            } catch (IOException e) {
                System.out.println("Trying again in 1.5 seconds");
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
        gateway.setConnectionData(master.getHostName(), master.getPort(1337));
    }

}


