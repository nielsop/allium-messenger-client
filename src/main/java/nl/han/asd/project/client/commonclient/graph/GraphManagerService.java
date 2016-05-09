package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

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
        this.currentGraphVersion = versionNumber;
    }

    public void processGraphUpdates() {
        UpdatedGraphResponseWrapper updatedGraph = gateway.getUpdatedGraph(currentGraphVersion);
        if (updatedGraph.getLast().newVersion > currentGraphVersion) {
            setCurrentGraphVersion(updatedGraph.getLast().newVersion);

            if (updatedGraph.getLast().isFullGraph) {
                graph.resetGraph();
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            } else {
                updatedGraph.getLast().deletedNodes.forEach(vertex -> graph.removeNodeVertex(vertex));
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            }
        }
    }


}
