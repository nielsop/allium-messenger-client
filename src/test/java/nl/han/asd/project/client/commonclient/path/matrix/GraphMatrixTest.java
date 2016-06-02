package nl.han.asd.project.client.commonclient.path.matrix;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.algorithm.GraphMatrix;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class GraphMatrixTest {
    @Mock
    private Map<String, Node> verticesMock;

    private GraphMatrix graphMatrix;
    private Map<String, Node> vertices = buildGraph();

    @Before
    public void setUp() {
        graphMatrix = new GraphMatrix(vertices);
        graphMatrix.fillAndCalculateMatrix();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() throws Exception {
        GraphMatrix graphMatrix = new GraphMatrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToSmallGraph() throws Exception {
        when(verticesMock.size()).thenReturn(1);
        GraphMatrix graphMatrix = new GraphMatrix(verticesMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindIndexOfKeyThatDoesNotExists() {
        graphMatrix.findIndexOfKey("Z");
    }

    public Map<String, Node> buildGraph() {
        Graph graph = new Graph();

        Node nodeA = new Node("A", "127.0.0.1", 1001, new byte[] { 0x00 });
        Node nodeB = new Node("B", "127.0.0.1", 1002, new byte[] { 0x00 });
        Node nodeC = new Node("C", "127.0.0.1", 1003, new byte[] { 0x00 });
        Node nodeD = new Node("D", "127.0.0.1", 1004, new byte[] { 0x00 });
        Node nodeE = new Node("E", "127.0.0.1", 1005, new byte[] { 0x00 });
        Node nodeF = new Node("F", "127.0.0.1", 1006, new byte[] { 0x00 });
        Node nodeG = new Node("G", "127.0.0.1", 1006, new byte[] { 0x00 });
        Node nodeH = new Node("H", "127.0.0.1", 1007, new byte[] { 0x00 });

        nodeA.addEdge(nodeE, 8);
        nodeA.addEdge(nodeF, 4);
        nodeE.addEdge(nodeC, 3);
        nodeC.addEdge(nodeD, 1);
        nodeC.addEdge(nodeB, 10);
        nodeF.addEdge(nodeB, 2);
        nodeF.addEdge(nodeD, 3);
        nodeG.addEdge(nodeE, 1);
        nodeG.addEdge(nodeC, 1);

        graph.addNodeVertex(nodeA);
        graph.addNodeVertex(nodeB);
        graph.addNodeVertex(nodeC);
        graph.addNodeVertex(nodeD);
        graph.addNodeVertex(nodeE);
        graph.addNodeVertex(nodeF);
        graph.addNodeVertex(nodeG);
        graph.addNodeVertex(nodeH);

        return graph.getGraphMap();
    }
}
