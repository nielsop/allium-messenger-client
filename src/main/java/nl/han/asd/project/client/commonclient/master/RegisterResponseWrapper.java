package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class RegisterResponseWrapper {
    public HanRoutingProtocol.ClientRegisterResponse.Status status;
    public RegisterResponseWrapper(HanRoutingProtocol.ClientRegisterResponse.Status status) {
        this.status = status;
    }
}
