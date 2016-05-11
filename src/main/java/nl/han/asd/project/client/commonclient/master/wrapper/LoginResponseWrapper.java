package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * Wraps the HanRoutingProtocol Login response.
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 22-4-2016
 */
public class LoginResponseWrapper {

    /**
     * Contains a list of all connected nodes.
     */
    public List<HanRoutingProtocol.Node> nodeList;

    /**
     * Stores the secret hash.
     */
    public String secretHash;

    /**
     * Stores the status of the login response.
     */
    public HanRoutingProtocol.ClientLoginResponse.Status status;

    /**
     * Creates a new LoginResponseWrapper object.
     *
     * @param nodeList   The connected nodes list.
     * @param secretHash The secret hash.
     * @param status     The status of the login request.
     */
    public LoginResponseWrapper(List<HanRoutingProtocol.Node> nodeList, String secretHash,
            HanRoutingProtocol.ClientLoginResponse.Status status) {
        this.nodeList = nodeList;
        this.secretHash = secretHash;
        this.status = status;
    }
}
