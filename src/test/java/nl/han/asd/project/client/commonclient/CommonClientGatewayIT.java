package nl.han.asd.project.client.commonclient;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.IMessageConfirmation;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class CommonClientGatewayIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGatewayIT.class);
    private CloudHost master;
    private MasterGateway gateway;
    private CommonClientGateway commonClientGateway;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IEncryptionService encryptionService;
    private IRegistration registration;
    private ILoginService login;
    private ISendMessage sendMessage;
    private IMessageBuilder messageBuilder;
    private IMessageConfirmation messageConfirmation;

    private String validUsername = "validUsername";
    private String validPassword = "validPassword";

    @Before
    public void setup() {
        createCloudHost();
        Injector injector = Guice.createInjector(new CommonClientModule());
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

    @After
    public void after() {
        master.teardown();
    }

    @Test
    @Ignore("Fix?")
    public void testRegisterRequestSuccessful() throws Exception {
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
                LOGGER.error(e.getMessage(), e);
            }
        }
//        gateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
//        gateway.setConnectionData(master.getHostName(), master.getPort(1337));
    }

}


