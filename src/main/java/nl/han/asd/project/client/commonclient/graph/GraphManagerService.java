package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.IGetGraphUpdates;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

public class GraphManagerService implements IGetVertices {

    private int currentGraphVersion;
    private Graph graph;
    private IGetGraphUpdates gateway;

    @Inject
    public GraphManagerService(IGetGraphUpdates gateway) {
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
        if (updatedGraph.getLast().getNewVersion() > currentGraphVersion) {
            setCurrentGraphVersion(updatedGraph.getLast().getNewVersion());

            if (updatedGraph.getLast().isFullGraph()) {
                graph.resetGraph();
                updatedGraph.getLast().getAddedNodes().forEach(vertex -> graph.addNodeVertex(vertex));
            } else {
                updatedGraph.getLast().getDeletedNodes().forEach(vertex -> graph.removeNodeVertex(vertex));
                updatedGraph.getLast().getAddedNodes().forEach(vertex -> graph.addNodeVertex(vertex));
            }
        }
    }

}