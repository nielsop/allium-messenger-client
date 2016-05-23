package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.IAuthentication;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.node.ISetConnectedNodes;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 18-4-2016
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    public MasterGateway gateway;

    @Mock
    public ISetConnectedNodes connectedNodes;

    @Mock
    public IAuthentication authentication;

    private LoginService loginService;

    @Test
    public void test() {
    }

    @Test
    public void testLoginWithValidUsernameAndPassword() {
        final LoginService loginService = new LoginService(connectedNodes, authentication, gateway);
        final LoginResponseWrapper loginResponse = new LoginResponseWrapper(new ArrayList<>(), "",
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Mockito.when(gateway.authenticate("niels", "test")).thenReturn(loginResponse);
        Assert.assertEquals(HanRoutingProtocol.ClientLoginResponse.Status.SUCCES, loginService.login("niels", "test").status);

    }
}
