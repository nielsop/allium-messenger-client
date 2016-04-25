package master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.client.commonclient.master.MasterGateway;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
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
        gateway = new MasterGateway("195.169.194.234", 32886);
    }

    @AfterClass
    public static void finish() {
        gateway.close();
    }

    @Test
    public void testRegisterClientSuccessful() {
        Assert.assertEquals(gateway.register(VALID_USERNAME, VALID_PASSWORD).getStatus(),
                HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES);
    }

    @Test
    public void testRegisterClientUsernameTaken() {
        gateway.register(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertEquals(gateway.register(VALID_USERNAME, VALID_PASSWORD).getStatus(),
                HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME);
    }

    @Test
    public void testLoginSuccessful() {
        Assert.assertTrue(gateway.authenticate(VALID_USERNAME, VALID_PASSWORD).status ==
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }
}
