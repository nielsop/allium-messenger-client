package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

    @Override
    public HanRoutingProtocol.ClientLoginResponse authenticateUser(String username, String password, String publicKey) {
        return null;
    }
}
