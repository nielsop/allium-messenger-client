package nl.han.asd.project.client.commonclient.graph;

import java.io.IOException;

import javax.inject.Inject;

import com.google.protobuf.InvalidProtocolBufferException;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.connection.Parser;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdate;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateResponse;

public class GraphManagerService {

    private int currentGraphVersion;
    private Graph graph;
    private MasterGateway gateway;

    @Inject
    public GraphManagerService(MasterGateway gateway) {
        graph = new Graph();
        this.gateway = gateway;
        currentGraphVersion = 0;
    }

    public int getCurrentGraphVersion() {
        return currentGraphVersion;
    }

    public void setCurrentGraphVersion(int versionNumber) {
        currentGraphVersion = versionNumber;
    }

    private GraphUpdate getLastUpdate(GraphUpdateResponse response) throws InvalidProtocolBufferException {
        int lastId = response.getGraphUpdatesCount() - 1;
        return (GraphUpdate) Parser.parseFrom(response.getGraphUpdates(lastId).toByteArray(), GraphUpdate.class);
    }

    public void processGraphUpdates() throws IOException, MessageNotSentException {
        GraphUpdateRequest.Builder requestBuilder = GraphUpdateRequest.newBuilder();
        requestBuilder.setCurrentVersion(currentGraphVersion);

        GraphUpdateResponse graphUpdateResponse = gateway.getUpdatedGraph(requestBuilder.build());
        GraphUpdate lastGraphUpdate = getLastUpdate(graphUpdateResponse);

        if (lastGraphUpdate.getNewVersion() > currentGraphVersion) {
            setCurrentGraphVersion(lastGraphUpdate.getNewVersion());

            if (lastGraphUpdate.getIsFullGraph()) {
                graph.resetGraph();
                lastGraphUpdate.getAddedNodesList().forEach(graph::addNodeVertex);
            } else {
                lastGraphUpdate.getDeletedNodesList().forEach(graph::removeNodeVertex);
                lastGraphUpdate.getAddedNodesList().forEach(graph::addNodeVertex);
            }
        }
    }

}
