package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.connection.Parser;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateRequest;

import java.io.IOException;
import java.util.Map;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;

public class GraphManagerService implements IGetVertices {
    private int currentGraphVersion;
    private Graph graph;
    private IGetUpdatedGraph getUpdatedGraph;

    @Inject
    public GraphManagerService(IGetUpdatedGraph gateway) {
        graph = new Graph();
        this.getUpdatedGraph = Check.notNull(gateway, "getUpdatedGraph");
        currentGraphVersion = 0;
    }

    /**
     * @return the current graph version
     */
    public int getCurrentGraphVersion() {
        return currentGraphVersion;
    }

    private void setCurrentGraphVersion(int versionNumber) {
        currentGraphVersion = versionNumber;
    }

    /**
     * This method processes the grapUpdates that are retrieved from the Master application.
     * The addedNodes are iterated over twice.
     * The first iteration assures that all node objects are made.
     * The second iteration makes it possible to add the right edges to the right nodes.
     *
     * @throws IOException
     * @throws MessageNotSentException
     */
    public void processGraphUpdates() throws IOException, MessageNotSentException {
        GraphUpdateResponse response = getUpdatedGraph.getUpdatedGraph(GraphUpdateRequest.newBuilder().setCurrentVersion(currentGraphVersion).build());
        GraphUpdate lastUpdate = Parser.parseFrom(response.getGraphUpdates(response.getGraphUpdatesCount() - 1).toByteArray(), GraphUpdate.class);

        if (lastUpdate.getNewVersion() <= currentGraphVersion)
            return;
        if (lastUpdate.getIsFullGraph())
            graph.resetGraph();

        for (ByteString updateByteString : response.getGraphUpdatesList()) {
            GraphUpdate update = Parser.parseFrom(updateByteString.toByteArray(), GraphUpdate.class);
            for (nl.han.asd.project.protocol.HanRoutingProtocol.Node addedNode : update.getAddedNodesList()) {
                graph.addNodeVertex(addedNode);
                graph.addEdgesToVertex(addedNode);
            }
            for (nl.han.asd.project.protocol.HanRoutingProtocol.Node deletedNode : update.getDeletedNodesList()) {
                graph.removeNodeVertex(deletedNode);
            }
        }
        currentGraphVersion = lastUpdate.getNewVersion();
    }

    /**
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return the vertices from the graph
     */
    @Override
    public Map<String, Node> getVertices() {
        return graph.getVertexMap();
    }
}
