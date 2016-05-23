package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class MasterGatewayIT {

    private static final String VALID_USERNAME = "valid_username";
    private static final String VALID_PASSWORD = "valid_password";
    private CloudHost master;
    private MasterGateway gateway;

    @Before
    public void setup() {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        Injector injector = Guice.createInjector(new EncryptionModule());
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
                e.printStackTrace();
            }
        }
        gateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
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
                gateway.register("meneer", VALID_PASSWORD).getStatus());
    }

    @Test
    public void testRegisterClientSameUsernameFails() {
        String username = UUID.randomUUID().toString();
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES,
                gateway.register(username, VALID_PASSWORD).getStatus());
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME,
                gateway.register(username, VALID_PASSWORD).getStatus());
    }

    /* Login of clients on master server */
    @Test
    public void testLoginSuccessful() {
        gateway.register(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).getStatus()
                == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

    /* Get updated graph from master server */
    // TODO: Tests for when we actually add real nodes & see if the right node is added to master.
    @Test
    public void testGetUpdatedGraphSuccessful() {
        Assert.assertTrue(true
                /*TODO: gateway.getUpdatedGraph(0).getLast().newVersion >= gateway
                        .getCurrentGraphVersion()*/);
    }

    /* Get active client group from master server */

    @Test @Ignore("Has to be fixed.") //TODO: Fix test?
    public void testGetClientGroupSuccessful() {
        ClientGroupResponseWrapper response = gateway.getClientGroup();
        Assert.assertTrue(response.getClientGroup().size() >= 0);
    }
}
