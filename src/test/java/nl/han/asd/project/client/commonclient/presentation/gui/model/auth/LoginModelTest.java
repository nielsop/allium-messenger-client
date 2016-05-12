package nl.han.asd.project.client.commonclient.presentation.gui.model.auth;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.utility.TestHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kenny
 * @version 1.0
 * @since 12-5-2016
 */
public class LoginModelTest {

    private LoginModel testLoginModel;
    private static GUI testGui = new GUI();

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

    @Test (expected = IllegalArgumentException.class)
    public void testGetLoginStatusInvalidUsername() throws Exception {
        testLoginModel.getLoginStatus(invalidUsername, validPassword);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetLoginStatusInvalidPassword() throws Exception {
        testLoginModel.getLoginStatus(validUsername, invalidPassword);
    }

}
