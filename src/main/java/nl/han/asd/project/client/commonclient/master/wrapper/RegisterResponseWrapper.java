package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class RegisterResponseWrapper {
    /**
     * Stores the status of the registerRequest request.
     */
    private HanRoutingProtocol.ClientRegisterResponse.Status status;

    /**
     * Creates a new RegisterResponseWrapper request
     *
     * @param status The status of the response.
     */
    public RegisterResponseWrapper(HanRoutingProtocol.ClientRegisterResponse.Status status) {
        this.status = status;
    }

    public HanRoutingProtocol.ClientRegisterResponse.Status getStatus() {
        return status;
    }
}
