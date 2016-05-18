package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.master.wrapper.UpdatedGraphResponseWrapper;

import java.util.Map;

public class GraphManagerService implements IGetVertices {

    private int currentGraphVersion;
    private Graph graph;
    private IGetUpdatedGraph gateway;

    @Inject
    public GraphManagerService(IGetUpdatedGraph gateway) {
        graph = new Graph();
        this.gateway = gateway;
        currentGraphVersion = 0;
    }

    /**
     *
     * @return the current graph version
     */
    public int getCurrentGraphVersion() {
        return currentGraphVersion;
    }

    private void setCurrentGraphVersion(int versionNumber) {
        this.currentGraphVersion = versionNumber;
    }

    /**
     * This method processes the grapUpdates that are retrieved from the Master application.
     * The addedNodes are iterated over twice.
     * The first iteration assures that all node objects are made.
     * The second iteration makes it possible to add the right edges to the right nodes.
     */
    public void processGraphUpdates() {
        UpdatedGraphResponseWrapper updatedGraph = gateway.getUpdatedGraph(currentGraphVersion);
        if (updatedGraph.getLast().newVersion > currentGraphVersion) {
            setCurrentGraphVersion(updatedGraph.getLast().newVersion);

            if (updatedGraph.getLast().isFullGraph) {
                graph.resetGraph();
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addEdgesToVertex(vertex));
            } else {
                updatedGraph.getLast().deletedNodes.forEach(vertex -> graph.removeNodeVertex(vertex));
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addNodeVertex(vertex));
                updatedGraph.getLast().addedNodes.forEach(vertex -> graph.addEdgesToVertex(vertex));
            }
        }
    }

    /**
     *
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     *
     * @return the vertices from the graph
     */
    @Override
    public Map<String,Node> getVertices() {
        return graph.getVertexMap();
    }
}
