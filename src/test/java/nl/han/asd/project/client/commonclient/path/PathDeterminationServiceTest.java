package nl.han.asd.project.client.commonclient.path;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.IGetVertices;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.store.Contact;

@RunWith(MockitoJUnitRunner.class)
public class PathDeterminationServiceTest {

    @Mock
    IGetVertices getVertices;

    @Mock
    IGetClientGroup getClientGroup;

    @Mock
    Contact contactReciever;

    @InjectMocks
    PathDeterminationService pathDeterminationService;

    @Test
    public void testNoDuplicateStartNodes() throws Exception {
        Map<String, Node> graph = buildGraph();
        when(getVertices.getVertices()).thenReturn(graph);

        when(contactReciever.getConnectedNodes()).thenReturn(new Node[] { graph.get("C") });

        List<Node> path = pathDeterminationService.getPath(0, contactReciever);
        List<Node> path2 = pathDeterminationService.getPath(0, contactReciever);

        Assert.assertNotEquals(path, path2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIGetVerticesNull() {
        PathDeterminationService pds = new PathDeterminationService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPathContactReceiverNull() {
        pathDeterminationService.getPath(0, null);
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

    //    @Before
    //    public void setUp() throws Exception {
    //        contact = new Contact("Username", "1234");
    //        contact.setConnectedNodes(new Node[] { new Node("NODE_ID_1", "192.168.2.8", 1234, "123456789".getBytes()),
    //                new Node("NODE_ID_2", "192.168.2.9", 1234, "123456789".getBytes()),
    //                new Node("NODE_ID_3", "192.168.2.10", 1234, "123456789".getBytes()) });
    //    }
    //
    //    /*
    //    Comparing self created path with a pathDeterminationService generated path
    //     */
    //    @Test
    //    public void whenMiniumHops() {
    //        //Node[] selfMadePath = {new Node(),new Node(),new Node()};
    //
    //        int minimunNodes = 3;
    //        ArrayList<Node> generatePath = pathDeterminationService.getPath(3, contact);
    //
    //        Assert.assertEquals(minimunNodes, generatePath.size());
    //    }
    //
    //    /*
    //Checking if generatedPath contains Node objects
    // */
    //    @Test
    //    public void checkIfGeneratedPathContainsNodes() {
    //        Node[] selfMadePath = { new Node("NODE_ID_1", "192.168.2.8", 1234, "123456789".getBytes()),
    //                new Node("NODE_ID_2", "192.168.2.9", 1234, "123456789".getBytes()),
    //                new Node("NODE_ID_3", "192.168.2.10", 1234, "123456789".getBytes()) };
    //
    //        ArrayList<Node> generatePath = pathDeterminationService.getPath(3, contact);
    //
    //        for (int i = 0; i < selfMadePath.length; i++) {
    //            Assert.assertEquals(selfMadePath[i], generatePath.get(i));
    //        }
    //    }
    //
    //    /*
    //Checking if error is trown when miniumHops is negative number
    //*/
    //    @Test(expected = IllegalArgumentException.class)
    //    public void whenMinimunHopsIsNegativeThrowError() {
    //        ArrayList<Node> generatePath = pathDeterminationService.getPath(-1, contact);
    //    }
    //
    //    /*
    //    Check if the first Node in the generated path is a ConnectedNode from the host client.
    //     */
    //    @Test
    //    public void firstNodeInPathIsAConnectedNodeFromHostClient() {
    //        ArrayList<Node> generatePath = pathDeterminationService.getPath(3, contact);
    //        Node[] contactConnectedNodes = new Node[0];
    //        try {
    //            contactConnectedNodes = contact.getConnectedNodes();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        Assert.assertTrue(inArray(generatePath.get(0), contactConnectedNodes));
    //    }
    //
    //    //    @Test
    //    //    public void clientHostConnectedNodesAreUpdatedIfLastUpdateIsExpired() {
    //    //        throw new NotImplementedException();
    //    //    }
    //
    //    private boolean inArray(Node needle, Node[] haystack) {
    //        for (Node n : haystack) {
    //            if (n == needle) {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    //
    //    @After
    //    public void tearDown() throws Exception {
    //
    //    }
}
