package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.utility.RequestWrapper;
import nl.han.asd.project.client.commonclient.utility.ResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.io.IOException;
import java.net.Socket;

public class MasterGateway implements IGetUpdatedGraph, IGetClients, IHeartbeat, IAuthentication {
    //TODO: missing: IWebService from Master
    public IEncrypt encrypt;

    @Override
    public HanRoutingProtocol.ClientLoginResponse authenticateUser(final String username, final String password,
                                                                   final String publicKey) {
        /* Code to prevent merge conflicts until we get dependency injection */
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (socket != null) {
            final HanRoutingProtocol.ClientLoginRequest.Builder loginRequestBuilder = HanRoutingProtocol.ClientLoginRequest.
                    newBuilder();
            loginRequestBuilder.setUsername(username).setPassword(password).setPublicKey(publicKey);

            final RequestWrapper loginRequest = new RequestWrapper(loginRequestBuilder.build(), socket);
            loginRequest.writeToSocket();

            final ResponseWrapper loginResponse = new ResponseWrapper(HanRoutingProtocol.EncryptedWrapper.Type.CLIENTLOGINRESPONSE, socket);
            return (HanRoutingProtocol.ClientLoginResponse) loginResponse.read();
        }
        return null;
    }
}
