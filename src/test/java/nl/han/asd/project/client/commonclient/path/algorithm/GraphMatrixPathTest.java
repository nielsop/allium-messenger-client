package nl.han.asd.project.client.commonclient.path.algorithm;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;

@RunWith(PowerMockRunner.class)
public class GraphMatrixPathTest {

    @Mock
    private Map<String, Node> verticesMock;

    @InjectMocks
    private GraphMatrixPath graphMatrixPathMock;

    private Map<String, Node> vertices;
    private GraphMatrix graphMatrix;
    private GraphMatrixPath graphMatrixPath;

    @Before
    public void setUp() {
        vertices = buildGraph();
        graphMatrix = new GraphMatrix(vertices, 10);
        graphMatrix.fillAndCalculateMatrix();
        graphMatrixPath = new GraphMatrixPath(vertices, graphMatrix);
    }

    @Test
    public void testConstructorNullGraphMatrix() throws Exception {
        new GraphMatrixPath(vertices, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullVertices() {
        new GraphMatrixPath(null, graphMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullStartNode() {
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(true);

        graphMatrixPathMock.findPath(null, tempEndNode);
    }

    @Test(expected = NodeNotInGraphException.class)
    public void testFindPathStartNodeNotInGraph() {
        Node tempStartNode = mock(Node.class);
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(true);

        when(tempStartNode.getId()).thenReturn("localhost:1025");
        when(verticesMock.containsKey(tempStartNode.getId())).thenReturn(false);

        graphMatrixPathMock.findPath(tempStartNode, tempEndNode);
    }

    @Test(expected = NodeNotInGraphException.class)
    public void testFindPathEndNodeNotInGraph() {
        Node tempStartNode = mock(Node.class);
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(false);

        when(tempStartNode.getId()).thenReturn("localhost:1025");
        when(verticesMock.containsKey(tempStartNode.getId())).thenReturn(true);

        graphMatrixPathMock.findPath(tempStartNode, tempEndNode);
    }

    @Test
    public void testEndAndSameNodeAreEqual() {
        Node tempNode = mock(Node.class);

        when(tempNode.getId()).thenReturn("localhost:1025");
        when(verticesMock.containsKey(tempNode.getId())).thenReturn(true);

        List<Node> result = graphMatrixPathMock.findPath(tempNode, tempNode);
        Assert.assertArrayEquals(new Node[] { tempNode, tempNode }, result.toArray());
    }

    @Test
    public void testNoPathDiscovered() {
        Map<String, Node> vertices = buildGraph();
        GraphMatrixPath dijkstra = new GraphMatrixPath(vertices, graphMatrix);

        Node startNode = vertices.get("A");
        Node endNode = vertices.get("H");

        List<Node> path = dijkstra.findPath(startNode, endNode);
        Assert.assertEquals(Collections.emptyList(), path);
    }

    @Test
    public void testPathDiscovered_AtoB() {
        Node[] expectedPath = new Node[] { vertices.get("A"), vertices.get("F"), vertices.get("B") };
        findAndAssertPath("A", "B", expectedPath);
    }

    @Test
    public void testPathDiscovered_AtoG() {
        Node[] expectedPath = new Node[] { vertices.get("A"), vertices.get("F"), vertices.get("D"), vertices.get("C"),
                vertices.get("G") };
        Node[] expectedPath2 = new Node[] { vertices.get("A"), vertices.get("E"), vertices.get("G") };
        findAndAssertPath("A", "G", expectedPath, expectedPath2);
    }

    @Test
    public void testPathDiscovered_BtoC() {
        Node[] expectedPath = new Node[] { vertices.get("B"), vertices.get("F"), vertices.get("D"), vertices.get("C") };
        findAndAssertPath("B", "C", expectedPath);
    }

    private void findAndAssertPath(String startNodeId, String endNodeId, Node[]... expectedPaths) {
        Node startNode = vertices.get(startNodeId);
        Node endNode = vertices.get(endNodeId);

        List<Node> path = graphMatrixPath.findPath(startNode, endNode);

        boolean result;
        for (Node[] nodeArray : expectedPaths) {
            result = Arrays.equals(nodeArray, path.toArray());

            if (result) {
                return;
            }
        }

        Assert.fail();
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
