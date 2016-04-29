package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

public class GraphManagerService {

    public int currentGraphVersion;
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

    public void setCurrentGraphVersion(int versionNumber){
        this.currentGraphVersion = versionNumber;
    }

    public void processGraphUpdates() {
        UpdatedGraphResponseWrapper updatedGraph = gateway.getUpdatedGraph(currentGraphVersion);
        if (updatedGraph.newVersion > currentGraphVersion) {
            setCurrentGraphVersion(updatedGraph.newVersion);

            if (updatedGraph.isFullGraph) {
                graph.resetGraph();
                updatedGraph.addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            } else {
                updatedGraph.deletedNodes.forEach(vertex -> graph.removeNodeVertex(vertex));
                updatedGraph.addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
            }
        }
    }


}
