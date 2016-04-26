package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 25-4-2016
 */
public class ClientGroupResponseWrapper {
    public List<HanRoutingProtocol.Client> clientGroup;

    public ClientGroupResponseWrapper(List<HanRoutingProtocol.Client> clientGroup) {
        this.clientGroup = clientGroup;
    }
}
