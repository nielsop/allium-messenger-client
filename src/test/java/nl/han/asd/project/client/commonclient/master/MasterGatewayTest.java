package nl.han.asd.project.client.commonclient.master;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.Socket;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterGateway.class)
public class MasterGatewayTest {

    private static final String VALID_USERNAME_3CHARS = "OKE";
    private static final String VALID_USERNAME_50CHARS = "ThisIsAUsernameOf50CharactersIIIIIIIIIIIIIIIIIIIII";
    private static final String INVALID_USERNAME_2CHARS = "NO";
    private static final String INVALID_USERNAME_51CHARS = "ThisIsAUsernameOf51CharactersIIIIIIIIIIIIIIIIIIIIII";
    private static final String VALID_USERNAME_RIGHT_CHARS = "This-Is_Valid-Username";
    private static final String INVALID_USERNAME_WRONG_CHARS = "WrongUsername*&^%$";

    private static final String INVALID_PASSWORD_7CHARS = "Invalid";
    private static final String VALID_PASSWORD_8CHARS = "ThisIsOk";
    private static final String VALID_PASSWORD_40CHARS = "ThisIsAPasswordOf40CharactersOOOOOOOOOOO";
    private static final String INVALID_PASSWORD_41CHARS = "ThisIsAPasswordOf41CharactersOOOOOOOOOOOO";
    private static final String VALID_PASSWORD_RIGHT_CHARS = "This-Is_Valid-Password";
    private static final String INVALID_PASSWORD_WRONG_CHARS = "WrongPassword*&@#$";

    private MasterGateway gateway;
    private CloudHost master;

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
                System.out.println("Trying again in 1.5 second");
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

    @After
    public void after() {
        master.teardown();
    }

    /* Testing registering username */
    @Test
    public void testMasterGatewayRegister2CharsUsernameFailed() {
        ClientRegisterResponse.Status status = gateway.register(INVALID_USERNAME_2CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    @Test
    public void testMasterGatewayRegister3CharsUsernameSuccess() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }

    @Test
    public void testMasterGatewayRegister40CharsUsernameSuccess() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_50CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }

    @Test
    public void testMasterGatewayRegister41CharsUsernameFailed() {
        ClientRegisterResponse.Status status = gateway.register(INVALID_USERNAME_51CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    @Test
    public void testMasterGatewayRegisterUsernameWithRightChars() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_RIGHT_CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }

    @Test
    public void testMasterGatewayRegisterUsernameWithWrongChars() {
        ClientRegisterResponse.Status status = gateway.register(INVALID_USERNAME_WRONG_CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    /* Testing registering password */
    @Test
    public void testMasterGatewayRegister7CharsPasswordFailed() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, INVALID_PASSWORD_7CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    @Test
    public void testMasterGatewayRegister8CharsPasswordSuccess() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, VALID_PASSWORD_8CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }

    @Test
    public void testMasterGatewayRegister40CharsPasswordSuccess() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, VALID_PASSWORD_40CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }

    @Test
    public void testMasterGatewayRegister41CharsPasswordFailed() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, INVALID_PASSWORD_41CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    @Test
    public void testMasterGatewayRegisterWrongCharsFailed() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, INVALID_PASSWORD_WRONG_CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.FAILED, status);
    }

    @Test
    public void testMasterGatewayRegisterRightCharsSucces() {
        ClientRegisterResponse.Status status = gateway.register(VALID_USERNAME_3CHARS, VALID_PASSWORD_RIGHT_CHARS).status;
        Assert.assertEquals(ClientRegisterResponse.Status.SUCCES, status);
    }
}
