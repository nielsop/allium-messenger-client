package nl.han.asd.project.client.commonclient.path.algorithm;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class DijkstraTest {

    @Mock
    private Map<String, Node> verticesMock;

    @InjectMocks
    private Dijkstra dijkstraMock;

    private Map<String, Node> vertices;
    private Dijkstra dijkstra;

    @Before
    public void setUp() {
        this.vertices = buildGraph();
        this.dijkstra = new Dijkstra(vertices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullVertices() {
        Dijkstra instance = new Dijkstra(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullStartNode() {
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(true);

        dijkstra.findPath(null, tempEndNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathNullEndNode() {
        Node tempStartNode = mock(Node.class);

        when(tempStartNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempStartNode.getId())).thenReturn(true);

        dijkstra.findPath(tempStartNode, null);
    }

    @Test(expected = IllegalArgumentException.class)
        public void testFindPathStartNodeNotInGraph() {
        Node tempStartNode = mock(Node.class);
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(true);

        when(tempStartNode.getId()).thenReturn("localhost:1025");
        when(verticesMock.containsKey(tempStartNode.getId())).thenReturn(false);

        dijkstra.findPath(tempStartNode, tempEndNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPathEndNodeNotInGraph() {
        Node tempStartNode = mock(Node.class);
        Node tempEndNode = mock(Node.class);

        when(tempEndNode.getId()).thenReturn("localhost:1024");
        when(verticesMock.containsKey(tempEndNode.getId())).thenReturn(false);

        when(tempStartNode.getId()).thenReturn("localhost:1025");
        when(verticesMock.containsKey(tempStartNode.getId())).thenReturn(true);

        dijkstra.findPath(tempStartNode, tempEndNode);
    }

    @Test
    public void testNoPathDiscovered() {
        Map<String, Node> vertices = buildGraph();
        Dijkstra dijkstra = new Dijkstra(vertices);

        Node startNode = vertices.get("A");
        Node endNode = vertices.get("H");

        List<Node> path = dijkstra.findPath(startNode, endNode);
        Assert.assertEquals(null, path);
    }

    @Test
    public void testPathDiscovered_AtoB() {
        Node[] expectedPath = new Node[] { vertices.get("A"), vertices.get("F"), vertices.get("B") };
        findAndAssertPath("A", "B", expectedPath);
    }

    @Test
    public void testPathDiscovered_AtoG() {
        Node[] expectedPath = new Node[] { vertices.get("A"), vertices.get("F"), vertices.get("D"), vertices.get("C"),  vertices.get("G") };
        Node[] expectedPath2 = new Node[] { vertices.get("A"), vertices.get("E"), vertices.get("G")};
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

        List<Node> path = dijkstra.findPath(startNode, endNode);

        boolean result = false;
        for(Node[] nodeArray : expectedPaths) {
            result = (Arrays.asList(nodeArray).equals(path));
            if (result) return;
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
