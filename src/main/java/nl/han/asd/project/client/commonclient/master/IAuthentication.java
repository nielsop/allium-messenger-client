package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

public interface IAuthentication {
    HanRoutingProtocol.ClientLoginResponse authenticateUser(String username, String password, String publicKey);
}
