package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class MasterGatewayIT {

    private static final String VALID_USERNAME = "Nielsje41";
    private static final String VALID_PASSWORD = "wachtwoord";
    private static MasterGateway gateway;

    @BeforeClass
    public static void setup() {
        Injector injector = Guice.createInjector(new EncryptionModule());
        gateway = new MasterGateway("10.182.5.216", 1337, injector.getInstance(IEncryptionService.class));
    }

    @AfterClass
    public static void finish() {
        gateway.close();
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
        Assert.assertTrue(gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).status ==
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

    /* Get updated graph from master server */
    // TODO: Tests for when we actually add real nodes & see if the right node is added to master.
    @Test
    public void testGetUpdatedGraphSuccessful() {
        Assert.assertTrue(gateway.getUpdatedGraph().newVersion >= gateway.getCurrentGraphVersion());
    }

    /* Get active client group from master server */

    @Test
    public void testGetClientGroupSuccessful() {
        Assert.assertTrue(gateway.getClientGroup().clientGroup.size() >= 0);
    }
    /* */
}
