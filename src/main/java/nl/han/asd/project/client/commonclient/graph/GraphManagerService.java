package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class GraphManagerService {

    public int currentGraphVersion = -1;
    private Graph graph;
    private MasterGateway gateway;

    @Inject
    public GraphManagerService(MasterGateway gateway) {
        graph = new Graph();
        this.gateway = gateway;
    }

    public int getCurrentGraphVersion() {
        return currentGraphVersion;
    }


    private void processGraphUpdates() {
        UpdatedGraphResponseWrapper updatedGraph = gateway.getUpdatedGraph(currentGraphVersion);
        if (updatedGraph.newVersion > currentGraphVersion) {
            if (updatedGraph.isFullGraph) {
                graph.resetGraph();
                updatedGraph.addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            } else {
                updatedGraph.addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            }
        }
    }


}
