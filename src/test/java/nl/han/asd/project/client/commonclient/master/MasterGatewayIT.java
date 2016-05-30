package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.CommonClientModule;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class MasterGatewayIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterGatewayIT.class);
    private static final String VALID_USERNAME = "valid_username";
    private static final String VALID_PASSWORD = "valid_password";
    private CloudHost master;
    private MasterGateway gateway;
    private IContactStore contactStore;
    private IEncryptionService encryptionService;

    @Before
    public void setup() {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        Injector injector = Guice.createInjector(new CommonClientModule());
        while (true) {
            try {
                new Socket(master.getHostName(), master.getPort(1337));
                break;
            } catch (IOException e) {
                System.out.println("Trying again in two seconds");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        encryptionService = injector.getInstance(IEncryptionService.class);
        gateway = new MasterGateway(encryptionService);
        contactStore = injector.getInstance(IContactStore.class);
        gateway.setConnectionData(master.getHostName(), master.getPort(1337));
    }

    @After
    public void after() {
        master.teardown();
    }

    /* Registration of clients on master server */
    @Test
    public void testRegisterClientSuccessful() {
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES,
                gateway.register("meneer", VALID_PASSWORD, VALID_PASSWORD).getStatus());
    }

    @Test
    public void testRegisterClientSameUsernameFails() {
        String username = UUID.randomUUID().toString();
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES,
                gateway.register(username, VALID_PASSWORD, VALID_PASSWORD).getStatus());
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME,
                gateway.register(username, VALID_PASSWORD, VALID_PASSWORD).getStatus());
    }

    /* Login of clients on master server */
    @Test
    public void testLoginSuccessful() {
        gateway.register(VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD);

        Assert.assertTrue(gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).getStatus()
                == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

    /* Get updated graph from master server */
    @Test
    public void testGetUpdatedGraphSuccessful() {
        Assert.assertTrue(gateway.IGetUpdatedGraph(0).getUpdatedGraphs().size() > 0);
    }

    /* Get active client group from master server */

    @Test
    @Ignore("Has to be fixed.")
    public void testGetClientGroupSuccessful() {
        ClientGroupResponseWrapper response = gateway.getClientGroup();
        Assert.assertTrue(response.getClientGroup().size() >= 0);
    }

    @Test
    public void testLogoutSuccessful() {
        gateway.register("test1234", "test1234", "test1234");
        LoginResponseWrapper loginResponse = gateway.authenticate("test1234", "test1234");
        CurrentUser currentUser = new CurrentUser("test1234", encryptionService.getPublicKey(), loginResponse.getSecretHash());
        contactStore.setCurrentUser(currentUser);
        Assert.assertTrue(gateway.logout(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername(), contactStore.getCurrentUser().getSecretHash()));
    }

    @Test
    public void testLogoutNotSuccessful() {
        Assert.assertFalse(gateway.logout("notLoggedInUser", "aRandomSecretHash"));
    }
}
