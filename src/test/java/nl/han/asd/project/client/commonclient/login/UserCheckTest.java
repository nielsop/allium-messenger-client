package nl.han.asd.project.client.commonclient.login;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserCheckTest {
    @Test(expected = IllegalArgumentException.class)
    public void checkUsernameNullUsername() throws Exception {
        UserCheck.checkUsername(null);
    }

    @Test(expected = IllegalUsernameException.class)
    public void checkUsernameTooShort() throws Exception {
        UserCheck.checkUsername("nm");
    }

    @Test(expected = IllegalUsernameException.class)
    public void checkUsernameTooLong() throws Exception {
        String username = "u";

        for (int i = 0; i < 2; i++) {
            username += "usernm";
        }

        UserCheck.checkUsername(username);
    }

    @Test(expected = IllegalUsernameException.class)
    public void checkUsernameNotAlphanumeric() throws Exception {
        UserCheck.checkUsername("_()username");
    }

    @Test
    public void checkUsernameValid() throws Exception {
        assertEquals("username", UserCheck.checkUsername("username"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkPasswordNullPassword() throws Exception {
        UserCheck.checkPassword(null);
    }

    @Test(expected = IllegalPasswordException.class)
    public void checkPasswordTooShort() throws Exception {
        UserCheck.checkPassword("psw");
    }

    @Test(expected = IllegalPasswordException.class)
    public void checkPasswordTooLong() throws Exception {
        String password = "u";

        for (int i = 0; i < 2; i++) {
            password += "password";
        }

        UserCheck.checkPassword(password);
    }

    @Test
    public void checkPasswordValid() throws Exception {
        assertEquals("password", UserCheck.checkPassword("password"));
    }
}
