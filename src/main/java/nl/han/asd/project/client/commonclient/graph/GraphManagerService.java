package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.connection.Parser;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdate;
import static nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateResponse;

public class GraphManagerService implements IGetVertices, IUpdateGraph {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphManagerService.class);
    public static final int PERIODIC_UPDATE = 600000;

    private int currentGraphVersion = 0;
    private static Graph graph;
    private IGetUpdatedGraph getUpdatedGraph;

    private long lastGraphUpdate = 0;
    private static final long MIN_TIMEOUT = 30000;
    private volatile boolean isRunning = true;

    @Inject
    public GraphManagerService(IGetUpdatedGraph gateway) {
        graph = new Graph();
        this.getUpdatedGraph = Check.notNull(gateway, "getUpdatedGraph");
        start();
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

        if (response.getGraphUpdatesCount() == 0) {
            return;
        }

        GraphUpdate lastUpdate = Parser.parseFrom(response.getGraphUpdates(response.getGraphUpdatesCount() - 1).toByteArray(), GraphUpdate.class);

        if (lastUpdate.getIsFullGraph()) {
            graph.resetGraph();
        }

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
        return graph.getGraphMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGraph() {
        if (System.currentTimeMillis() - lastGraphUpdate > MIN_TIMEOUT) {
            try {
                processGraphUpdates();
                lastGraphUpdate = System.currentTimeMillis();
            } catch (IOException | MessageNotSentException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("!! GRAPH MANAGER SERVICE STARTED");
                while (isRunning) {
                    try {
                        Thread.sleep(PERIODIC_UPDATE);
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    updateGraph();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        isRunning = false;
    }
}
