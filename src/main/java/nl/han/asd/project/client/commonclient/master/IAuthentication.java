package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;

public interface IAuthentication {
    HanRoutingProtocol.ClientLoginResponse authenticateUser(String username, String password, String publicKey) throws IOException;
}
