package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Marius on 25-04-16.
 */
public class RegisterResponseWrapper {
    private HanRoutingProtocol.ClientRegisterResponse.Status status;

    public RegisterResponseWrapper(HanRoutingProtocol.ClientRegisterResponse.Status status) {
        this.status = status;
    }

    public HanRoutingProtocol.ClientRegisterResponse.Status getStatus() {
        return status;
    }
}
