//package nl.han.asd.project.client.commonclient.graph;
//
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.protobuf.ByteString;
//import com.xebialabs.overcast.host.CloudHost;
//
//import nl.han.asd.project.client.commonclient.master.MasterGateway;
//import nl.han.asd.project.protocol.HanRoutingProtocol;
//import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateResponse;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ GraphUpdateResponse.class })
//public class GraphManagerServiceTest {
//
//    private GraphManagerService graphManagerService;
//    private CloudHost master;
//    private static final Logger LOGGER = LoggerFactory.getLogger(GraphManagerService.class);
//    private final ByteString PUBLICKEY = ByteString.copyFrom("123456789".getBytes());
//
//    @Mock
//    GraphUpdateResponse updatedGraphResponseWrapper;
//
//    @Mock
//    MasterGateway masterGateway;
//
//    @Test
//    public void testProcessGraphFullGraph() throws Exception {
//        List<HanRoutingProtocol.Node> addedNodes = new ArrayList<>();
//        List<GraphUpdateResponse> updatedGraphs = new ArrayList<>();
//
//        HanRoutingProtocol.Edge.Builder edge_1 = HanRoutingProtocol.Edge.newBuilder();
//        edge_1.setTargetNodeId("NODE_ID_2");
//        edge_1.setWeight(12);
//
//        HanRoutingProtocol.Edge.Builder edge_2 = HanRoutingProtocol.Edge.newBuilder();
//        edge_2.setTargetNodeId("NODE_ID_1");
//        edge_2.setWeight(6);
//
//        HanRoutingProtocol.Node.Builder node_1 = HanRoutingProtocol.Node.newBuilder();
//        node_1.setPort(1);
//        node_1.setIPaddress("192.168.2.1");
//        node_1.addEdge(edge_1);
//        node_1.setId("NODE_ID_1");
//        node_1.setPublicKey(PUBLICKEY);
//        addedNodes.add(node_1.build());
//
//        HanRoutingProtocol.Node.Builder node_2 = HanRoutingProtocol.Node.newBuilder();
//        node_2.setPort(2);
//        node_2.setIPaddress("192.168.2.2");
//        node_2.addEdge(edge_2);
//        node_2.setId("NODE_ID_2");
//        node_2.setPublicKey(PUBLICKEY);
//
//        addedNodes.add(node_2.build());
//
//        HanRoutingProtocol.Node.Builder node_3 = HanRoutingProtocol.Node.newBuilder();
//        node_3.setPort(3);
//        node_3.setIPaddress("192.168.2.3");
//        node_3.setId("NODE_ID_3");
//        node_3.setPublicKey(PUBLICKEY);
//
//        addedNodes.add(node_3.build());
//
//        HanRoutingProtocol.GraphUpdate.Builder graphUpdate = HanRoutingProtocol.GraphUpdate.newBuilder();
//        graphUpdate.addAllAddedNodes(addedNodes);
//        graphUpdate.setNewVersion(1);
//        graphUpdate.setIsFullGraph(true);
//
//        GraphUpdateResponse responseMock = mock(GraphUpdateResponse.class);
//        updatedGraphs.add(responseMock);
//
//        when(responseMock.getGraphUpdatesList()).thenReturn(updatedGraphs);
//
//        updatedGraphResponseWrapper.setUpdatedGraphs(updatedGraphs);
//        when(updatedGraphResponseWrapper.getLast()).thenReturn(updatedGraphs.get(updatedGraphs.size() - 1));
//        when(masterGateway.getUpdatedGraph(anyInt())).thenReturn(updatedGraphResponseWrapper);
//        when(updatedGraphResponseWrapper.getUpdatedGraphs()).thenReturn(updatedGraphs);
//        graphManagerService.processGraphUpdates();
//        Assert.assertEquals(graphManagerService.getCurrentGraphVersion(), 1);
//        Assert.assertEquals(graphManagerService.getGraph().getVertexMapSize(), 3);
//    }
//
//    @Test
//    public void testProcessGraphNotFullGraph() throws Exception {
//        List<HanRoutingProtocol.Node> addedNodes = new ArrayList<>();
//        List<GraphUpdateResponse> updatedGraphs = new ArrayList<>();
//
//        HanRoutingProtocol.Edge.Builder edge_1 = HanRoutingProtocol.Edge.newBuilder();
//        edge_1.setTargetNodeId("NODE_ID_2");
//        edge_1.setWeight(12);
//
//        HanRoutingProtocol.Edge.Builder edge_2 = HanRoutingProtocol.Edge.newBuilder();
//        edge_2.setTargetNodeId("NODE_ID_1");
//        edge_2.setWeight(6);
//
//        HanRoutingProtocol.Node.Builder node_1 = HanRoutingProtocol.Node.newBuilder();
//        node_1.setPort(1);
//        node_1.setIPaddress("192.168.2.1");
//        node_1.addEdge(edge_1);
//        node_1.setId("NODE_ID_1");
//        node_1.setPublicKey(PUBLICKEY);
//        addedNodes.add(node_1.build());
//
//        HanRoutingProtocol.GraphUpdate.Builder graphUpdate = HanRoutingProtocol.GraphUpdate.newBuilder();
//        graphUpdate.addAllAddedNodes(addedNodes);
//        graphUpdate.setNewVersion(1);
//        graphUpdate.setIsFullGraph(false);
//
//        updatedGraphs.add(new UpdatedGraphWrapper(graphUpdate.build()));
//        updatedGraphResponseWrapper.setUpdatedGraphs(updatedGraphs);
//        when(updatedGraphResponseWrapper.getLast()).thenReturn(updatedGraphs.get(updatedGraphs.size() - 1));
//        when(masterGateway.getUpdatedGraph(anyInt())).thenReturn(updatedGraphResponseWrapper);
//        when(updatedGraphResponseWrapper.getUpdatedGraphs()).thenReturn(updatedGraphs);
//        graphManagerService.processGraphUpdates();
//        Assert.assertEquals(graphManagerService.getCurrentGraphVersion(), 1);
//        Assert.assertEquals(graphManagerService.getGraph().getVertexMapSize(), 1);
//    }
//
//}
