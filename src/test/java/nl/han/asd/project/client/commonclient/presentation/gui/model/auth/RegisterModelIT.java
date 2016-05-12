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
public class RegisterModelIT {

    private static GUI testGui = new GUI();
    private static String validPassword;
    private static String invalidUsername;
    private static String invalidPassword;
    private RegisterModel testRegisterModel;
    private String validUsername;

    @BeforeClass
    public static void setUpOnce() throws Exception {
        testGui = new GUI();
        Injector injector = Guice.createInjector(new CommonclientModule());
        injector.injectMembers(testGui);
        validPassword = "TestPassword";
        invalidUsername = "AWayTooLongUsernameIsInvalid";
        invalidPassword = "PasswordIsBullshit*(#@^$*@#^(*!@#^!@#*(!^(*!@#";
    }

    @Before
    public void setUp() throws Exception {
        testRegisterModel = new RegisterModel(testGui);
        validUsername = TestHelper.createRandomValidUsername();
    }

    @Test
    public void testGetRegisterStatusValidUsernameAndPassword() throws Exception {
        HanRoutingProtocol.ClientRegisterResponse.Status registerStatus = testRegisterModel.getRegisterStatus(validUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES, registerStatus);
    }

    @Ignore("Master does not catch invalid username yet.")
    @Test
    public void testGetRegisterStatusInValidUsername() throws Exception {
        HanRoutingProtocol.ClientRegisterResponse.Status registerStatus = testRegisterModel.getRegisterStatus(invalidUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED, registerStatus);
    }

    @Ignore("Master does not catch invalid password yet.")
    @Test
    public void testGetRegisterStatusInValidPassword() throws Exception {
        HanRoutingProtocol.ClientRegisterResponse.Status registerStatus = testRegisterModel.getRegisterStatus(validUsername, invalidPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.FAILED, registerStatus);
    }

    @Test
    public void testGetLoginStatusTakenUsername() throws Exception {
        HanRoutingProtocol.ClientRegisterResponse.Status registerStatusOK = testRegisterModel.getRegisterStatus(validUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.SUCCES, registerStatusOK);
        HanRoutingProtocol.ClientRegisterResponse.Status registerStatusTakenUsername = testRegisterModel.getRegisterStatus(validUsername, validPassword);
        Assert.assertEquals(HanRoutingProtocol.ClientRegisterResponse.Status.TAKEN_USERNAME, registerStatusTakenUsername);
    }

}
