package nl.han.asd.project.client.commonclient.master.wrapper;

import com.google.protobuf.ByteString;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class UpdatedGraphResponseWrapperTest {

    private static UpdatedGraphResponseWrapper updatedGraphResponseWrapper;

    @BeforeClass
    public static void setupTestClass() {
        HanRoutingProtocol.Node node1 = HanRoutingProtocol.Node.newBuilder().setId("Node-1").setIPaddress("127.0.0.1").setPort(1337).setPublicKey("0x00").build();
        HanRoutingProtocol.Node node2 = HanRoutingProtocol.Node.newBuilder().setId("Node-2").setIPaddress("127.0.0.2").setPort(1337).setPublicKey("0x00").build();
        HanRoutingProtocol.Node node3 = HanRoutingProtocol.Node.newBuilder().setId("Node-3").setIPaddress("127.0.0.3").setPort(1337).setPublicKey("0x00").build();
        HanRoutingProtocol.Node node4 = HanRoutingProtocol.Node.newBuilder().setId("Node-4").setIPaddress("127.0.0.4").setPort(1337).setPublicKey("0x00").build();
        HanRoutingProtocol.Node node5 = HanRoutingProtocol.Node.newBuilder().setId("Node-5").setIPaddress("127.0.0.5").setPort(1337).setPublicKey("0x00").build();

        /*
        HanRoutingProtocol.GraphUpdate graphUpdate1 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(true).setNewVersion(1).setAddedNodes(0, node1).setAddedNodes(1, node2).build();
        HanRoutingProtocol.GraphUpdate graphUpdate2 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(2).setDeletedNodes(0, node1).setAddedNodes(0, node3).build();
        HanRoutingProtocol.GraphUpdate graphUpdate3 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(3).setAddedNodes(0, node4).setAddedNodes(1, node5).setDeletedNodes(0, node2).setDeletedNodes(1, node3).build();
        */

        HanRoutingProtocol.GraphUpdate graphUpdate1 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(1).build();
        HanRoutingProtocol.GraphUpdate graphUpdate2 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(2).build();
        HanRoutingProtocol.GraphUpdate graphUpdate3 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(3).build();
        List<ByteString> byteStringList = new ArrayList<>();
        byteStringList.add(graphUpdate1.toByteString());
        byteStringList.add(graphUpdate2.toByteString());
        byteStringList.add(graphUpdate3.toByteString());
        updatedGraphResponseWrapper = new UpdatedGraphResponseWrapper(byteStringList);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesNewVersion() {
        UpdatedGraphResponseWrapper.UpdatedGraphWrapper updatedGraph1 =
                updatedGraphResponseWrapper.updatedGraphs.get(0);
        Assert.assertEquals(updatedGraph1.newVersion, 1);
    }


    @Test
    public void testUpdatedGraphWrapperCreationSavesIsFullGraphTrue() {
        UpdatedGraphResponseWrapper.UpdatedGraphWrapper updatedGraph1 = updatedGraphResponseWrapper.updatedGraphs.get(0);
        Assert.assertEquals(updatedGraph1.isFullGraph, false);
//        Assert.assertEquals(updatedGraph1.isFullGraph, true);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesIsFullGraphFalse() {
        UpdatedGraphResponseWrapper.UpdatedGraphWrapper updatedGraph2 = updatedGraphResponseWrapper.updatedGraphs.get(1);
        Assert.assertEquals(updatedGraph2.isFullGraph, false);
    }


    @Test
    public void testUpdatedGraphWrapperCreationSavesAddedNodesList() {
        UpdatedGraphResponseWrapper.UpdatedGraphWrapper updatedGraph1 = updatedGraphResponseWrapper.updatedGraphs.get(0);
//        Assert.assertEquals(updatedGraph1.addedNodes.size(), 2);
        Assert.assertEquals(updatedGraph1.addedNodes.size(), 0);
    }


    @Test
    public void testUpdatedGraphWrapperCreationSavesDeletedNodesList() {
        UpdatedGraphResponseWrapper.UpdatedGraphWrapper updatedGraph2 = updatedGraphResponseWrapper.updatedGraphs.get(1);
//        Assert.assertEquals(updatedGraph2.deletedNodes.size(), 2);
        Assert.assertEquals(updatedGraph2.deletedNodes.size(), 0);
    }

}