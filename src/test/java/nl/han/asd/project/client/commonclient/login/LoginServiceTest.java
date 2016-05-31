package nl.han.asd.project.client.commonclient.login;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Contact.class, LoginService.class })
public class LoginServiceTest {

    /* Valid credentials */
    private static final String VALID_USERNAME = "TestUsername";
    private static final String VALID_PASSWORD = "TestPassword";
    /* Invalid credentials */
    private static final String INVALID_USERNAME_EMPTY = "";
    private static final String INVALID_PASSWORD_EMPTY = "";
    private static final String INVALID_USERNAME_FORBIDDEN_CHARACTERS = "Test^Username";
    private static final String INVALID_USERNAME_TOO_LONG = ""; // Aanname dat de maximum lengte van een username 12 tekens is.
    private static final String INVALID_PASSWORD_TOO_LONG = ""; // Aanname dat de maximum lengte van een password 16 tekens is.
    private static final String INVALID_USERNAME_TOO_SHORT = ""; // Aanname dat de minimum lengte van een username 3 tekens is.
    private static final String INVALID_PASSWORD_TOO_SHORT = ""; // Aanname dat de minimum lengte van een password 8 tekens is.
    private static final String INVALID_USERNAME_NULL = null;
    private static final String INVALID_PASSWORD_NULL = null;

    private IAuthentication authenticationMock;
    private IEncryptionService encryptionServiceMock;
    private ISetConnectedNodes setConnectedNodes;

    private ILoginService login;

    @Before
    public void setUp() {
        authenticationMock = mock(IAuthentication.class);
        encryptionServiceMock = mock(IEncryptionService.class);
        setConnectedNodes = mock(ISetConnectedNodes.class);
        login = new LoginService(authenticationMock, encryptionServiceMock, setConnectedNodes);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameEmpty() throws Exception {
        login.login(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordEmpty() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_EMPTY);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameTooLong() throws Exception {
        login.login(INVALID_USERNAME_TOO_LONG, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordTooLong() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_TOO_LONG);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testIsUsernameTooShort() throws Exception {
        login.login(INVALID_USERNAME_TOO_SHORT, VALID_PASSWORD);
    }

    @Test(expected = IllegalPasswordException.class)
    public void testIsPasswordTooShort() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_TOO_SHORT);
    }

    @Test(expected = IllegalUsernameException.class)
    public void testUsernameContainsForbiddenCharacters() throws Exception {
        login.login(INVALID_USERNAME_FORBIDDEN_CHARACTERS, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsUsernameNull() throws Exception {
        login.login(INVALID_USERNAME_NULL, VALID_PASSWORD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPasswordNull() throws Exception {
        login.login(VALID_USERNAME, INVALID_PASSWORD_NULL);
    }

    @Test(expected = InvalidCredentialsException.class)
    public void statusFailed() throws Exception {
        when(encryptionServiceMock.getPublicKey()).thenReturn("key".getBytes());

        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername(VALID_USERNAME);
        requestBuilder.setPassword(VALID_USERNAME);
        requestBuilder.setPublicKey(ByteString.copyFrom("key".getBytes()));
        ClientLoginRequest request = requestBuilder.build();

        ClientLoginResponse.Builder responseBuilder = ClientLoginResponse.newBuilder();
        responseBuilder.setStatus(ClientLoginResponse.Status.FAILED);
        ClientLoginResponse response = responseBuilder.build();

        when(authenticationMock.login(any())).thenReturn(response);

        login.login(VALID_USERNAME, VALID_PASSWORD);
    }

    @Test
    public void statusSuccess() throws Exception {
        when(encryptionServiceMock.getPublicKey()).thenReturn("key".getBytes());

        ClientLoginRequest.Builder requestBuilder = ClientLoginRequest.newBuilder();
        requestBuilder.setUsername(VALID_USERNAME);
        requestBuilder.setPassword(VALID_USERNAME);
        requestBuilder.setPublicKey(ByteString.copyFrom("key".getBytes()));
        ClientLoginRequest request = requestBuilder.build();

        ClientLoginResponse.Builder responseBuilder = ClientLoginResponse.newBuilder();
        responseBuilder.setStatus(ClientLoginResponse.Status.SUCCES);
        responseBuilder.setSecretHash("hash");
        responseBuilder.addConnectedNodes("127.0.0.1:1331");
        responseBuilder.addConnectedNodes("127.0.0.1:1332");
        responseBuilder.addConnectedNodes("127.0.0.1:1333");
        ClientLoginResponse response = responseBuilder.build();

        when(authenticationMock.login(any())).thenReturn(response);

        CurrentUser contactMock = mock(CurrentUser.class);
        whenNew(CurrentUser.class).withAnyArguments().thenReturn(contactMock);

        assertEquals(contactMock, login.login(VALID_USERNAME, VALID_PASSWORD));

        verify(setConnectedNodes).setConnectedNodes(eq(response.getConnectedNodesList()));
    }
}
