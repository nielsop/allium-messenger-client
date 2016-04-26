package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 25-4-2016
 */
public class UpdatedGraphResponseWrapper {

    public int newVersion;
    public boolean isFullGraph;
    public List<HanRoutingProtocol.Node> addedNodes;
    public List<HanRoutingProtocol.Node> deletedNodes;

    public UpdatedGraphResponseWrapper(int newVersion, boolean isFullGraph, List<HanRoutingProtocol.Node> addedNodes,
                                       List<HanRoutingProtocol.Node> deletedNodes) {
        this.newVersion = newVersion;
        this.isFullGraph = isFullGraph;
        this.addedNodes = addedNodes;
        this.deletedNodes = deletedNodes;
    }

}
