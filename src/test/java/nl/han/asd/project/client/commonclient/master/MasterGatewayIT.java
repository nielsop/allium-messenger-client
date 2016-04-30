package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.master.wrapper.ClientGroupResponseWrapper;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class MasterGatewayIT {

    private CloudHost master;
    private static final String VALID_USERNAME = "Nielsje41";
    private static final String VALID_PASSWORD = "wachtwoord";
    private static MasterGateway gateway;

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
                System.out.println("Trying again in 2 seconds");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gateway = new MasterGateway(master.getHostName(), master.getPort(1337), injector.getInstance(IEncryptionService.class));
    }

    @After
    public void aster() {
        master.teardown();
    }

    /* Registration of clients on master server */
    @Test
    public void testRegisterClientSuccessful() {
        Assert.assertEquals(gateway.register(VALID_USERNAME, VALID_PASSWORD).status,
                HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
    }

    @Test
    public void testRegisterClientUsernameTaken() {
        gateway.register(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertEquals(gateway.register(VALID_USERNAME, VALID_PASSWORD).status,
                HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME);
    }

    /* Login of clients on master server */
    @Test
    public void testLoginSuccessful() {
        gateway.register(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).status ==
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

    /* Get updated graph from master server */
    // TODO: Tests for when we actually add real nodes & see if the right node is added to master.
    @Test
    public void testGetUpdatedGraphSuccessful() {
        Assert.assertTrue(gateway.getUpdatedGraph().getLast().newVersion >= gateway.getCurrentGraphVersion());
    }

    /* Get active client group from master server */

    @Test
    public void testGetClientGroupSuccessful() {
        ClientGroupResponseWrapper response = gateway.getClientGroup();
        Assert.assertTrue(response.clientGroup.size() >= 0);
    }
    /* */
}
