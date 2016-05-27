package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * Wraps the HanRoutingProtocol ClientGroup response.
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 25-4-2016
 */
public class ClientGroupResponseWrapper {

    /**
     * Contains the group of clients.
     */
    private List<HanRoutingProtocol.Client> clientGroup;

    /**
     * Creates a new ClientGroupResponseWrapper object containing the clients in an easy to access list.
     *
     * @param clientGroup The new ClientGroupResponseWrapper object.
     */
    public ClientGroupResponseWrapper(List<HanRoutingProtocol.Client> clientGroup) {
        this.clientGroup = clientGroup;
    }

    public List<HanRoutingProtocol.Client> getClientGroup() {
        return clientGroup;
    }
}
