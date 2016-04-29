package integration.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;
import org.junit.rules.Timeout;

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
        Injector injector = Guice.createInjector(new CommonclientModule());
        gateway = new MasterGateway(
                injector.getInstance(IEncryptionService.class));
    }

    @AfterClass
    public static void finish() {
        gateway.close();
    }

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Test
    public void testRegisterClientSuccessful() {
        Assert.assertEquals(
                gateway.register(VALID_USERNAME, VALID_PASSWORD).getStatus(),
                HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
    }

    @Test
    public void testRegisterClientUsernameTaken() {
        Assert.assertEquals(
                gateway.register(VALID_USERNAME, VALID_PASSWORD).getStatus(),
                HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME);
    }

    @Test
    public void testLoginSuccessful() {
        Assert.assertTrue(
                gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).status
                        == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }
}
