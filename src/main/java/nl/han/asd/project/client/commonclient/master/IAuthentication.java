package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by DDulos on 18-Apr-16.
 */
public interface IAuthentication {
    public HanRoutingProtocol.ClientLoginResponse authUser(String username, String password, String publicKey);
}
