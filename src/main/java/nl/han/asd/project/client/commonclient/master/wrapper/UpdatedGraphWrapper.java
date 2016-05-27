package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

/**
 * @author Julius
 * @version 1.0
 * @since 10/05/16
 */
public class UpdatedGraphWrapper {
    /**
     * Stores the new version.
     */
    public int newVersion;

    /**
     * Stores whether or not the graph is a full graph.
     */
    public boolean isFullGraph;

    /**
     * Contains a list of all nodes that were added to the graph this version.
     */
    public List<HanRoutingProtocol.Node> addedNodes;

    /**
     * Contains a list of all nodes taht were deleted from the graph this version.
     */
    public List<HanRoutingProtocol.Node> deletedNodes;

    /**
     * Creates a new UpdatedGraphWrapper.
     *
     * @param graphUpdate The HRP updated graph.
     */
    public UpdatedGraphWrapper(HanRoutingProtocol.GraphUpdate graphUpdate) {
        this.newVersion = graphUpdate.getNewVersion();
        this.isFullGraph = graphUpdate.getIsFullGraph();
        this.addedNodes = graphUpdate.getAddedNodesList();
        this.deletedNodes = graphUpdate.getDeletedNodesList();
    }

    public int getNewVersion() {
        return newVersion;
    }

    public boolean isFullGraph() {
        return isFullGraph;
    }

    public List<HanRoutingProtocol.Node> getAddedNodes() {
        return addedNodes;
    }

    public List<HanRoutingProtocol.Node> getDeletedNodes() {
        return deletedNodes;
    }
}