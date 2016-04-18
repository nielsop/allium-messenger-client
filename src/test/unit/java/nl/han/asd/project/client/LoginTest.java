package nl.han.asd.project.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by DDulos on 13-Apr-16.
 */
public class LoginTest {
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "test1234";

    private static final String INCORRECT_USERNAME = "tester";
    private static final String INCORRECT_PASSWORD = "test12";

    @Test
    public void testLoginWithCorrectUsernameAndPassword() {
        assertEquals(1, 1);
    }

    @Test
    public void testLoginWithCorrectUsernameAndIncorrectPassword() {
        assertEquals(1, 1);
    }

    @Test
    public void testLoginWithIncorrectUsernameAndCorrectPassword() {
        assertEquals(1, 1);
    }

    @Test
    public void testLoginWithIncorrectUsernameAndPassword() {
        assertEquals(1, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoginWithEmptyUsername() {
        assertEquals(1, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoginWithEmptyPassword() {
        assertEquals(1, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLoginWithEmptyUsernamAndPassword() {
        assertEquals(1, 1);
    }

}
