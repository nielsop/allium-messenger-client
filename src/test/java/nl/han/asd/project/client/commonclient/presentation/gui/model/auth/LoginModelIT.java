package nl.han.asd.project.client.commonclient.presentation.gui.model.auth;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.utility.TestHelper;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 12-5-2016
 */

public class LoginModelIT {

    private static GUI testGui = new GUI();
    private LoginModel testLoginModel;
    private String validUsername;
    private String validPassword = "TestPassword";
    private String invalidUsername = "AWayTooLongUsernameIsInvalid";
    private String invalidPassword = "AWayTooLongPasswordIsInvalid";

    @BeforeClass
    public static void setUpOnce() throws Exception {
        testGui = new GUI();
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(testGui);
    }

    @Before
    public void setUp() throws Exception {
        testLoginModel = new LoginModel(testGui);
        validUsername = TestHelper.createRandomValidUsername();
    }

    @Test
    public void testGetLoginStatusSuccess() throws Exception {
        HanRoutingProtocol.ClientLoginResponse.Status loginStatus = testLoginModel.getLoginStatus(validUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES, loginStatus);
    }

    @Ignore("Master does not catch invalid username yet.")
    @Test
    public void testGetLoginStatusInvalidUsername() throws Exception {
        HanRoutingProtocol.ClientLoginResponse.Status loginStatus = testLoginModel.getLoginStatus(invalidUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.FAILED, loginStatus);
    }

    @Ignore("Master does not catch invalid password yet.")
    @Test
    public void testGetLoginStatusInvalidPassword() throws Exception {
        HanRoutingProtocol.ClientLoginResponse.Status loginStatus = testLoginModel.getLoginStatus(validUsername, invalidPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.FAILED, loginStatus);
    }

}
