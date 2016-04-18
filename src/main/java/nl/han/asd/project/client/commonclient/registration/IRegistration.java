package nl.han.asd.project.client.commonclient.registration;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Kenny on 13-4-2016.
 */
public interface IRegistration {

    HanRoutingProtocol.ClientRegisterResponse register(String username, String password);
}
