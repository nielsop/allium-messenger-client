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

    private static final byte[] EMPTY_PUBLICKEY_BYTES = new byte[]{0x00};
    private static UpdatedGraphResponseWrapper updatedGraphResponseWrapper;

    @BeforeClass
    public static void setupTestClass() {
        HanRoutingProtocol.Node node1 = HanRoutingProtocol.Node.newBuilder().setId("Node-1").setIPaddress("127.0.0.1")
                .setPort(1337).setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        HanRoutingProtocol.Node node2 = HanRoutingProtocol.Node.newBuilder().setId("Node-2").setIPaddress("127.0.0.2")
                .setPort(1337).setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        HanRoutingProtocol.Node node3 = HanRoutingProtocol.Node.newBuilder().setId("Node-3").setIPaddress("127.0.0.3")
                .setPort(1337).setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        HanRoutingProtocol.Node node4 = HanRoutingProtocol.Node.newBuilder().setId("Node-4").setIPaddress("127.0.0.4")
                .setPort(1337).setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();
        HanRoutingProtocol.Node node5 = HanRoutingProtocol.Node.newBuilder().setId("Node-5").setIPaddress("127.0.0.5")
                .setPort(1337).setPublicKey(
                        ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build();

        /*
        HanRoutingProtocol.GraphUpdate graphUpdate1 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(true).setNewVersion(1).setAddedNodes(0, node1).setAddedNodes(1, node2).build();
        HanRoutingProtocol.GraphUpdate graphUpdate2 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(2).setDeletedNodes(0, node1).setAddedNodes(0, node3).build();
        HanRoutingProtocol.GraphUpdate graphUpdate3 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(false).setNewVersion(3).setAddedNodes(0, node4).setAddedNodes(1, node5).setDeletedNodes(0, node2).setDeletedNodes(1, node3).build();
        */

        HanRoutingProtocol.GraphUpdate graphUpdate1 = HanRoutingProtocol.GraphUpdate.newBuilder().
                setIsFullGraph(true).setNewVersion(1).build();
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

        UpdatedGraphWrapper updatedGraph1 = updatedGraphResponseWrapper.getUpdatedGraphs()
                .get(0);
        Assert.assertEquals(updatedGraph1.getNewVersion(), 1);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesIsFullGraphTrue() {
        UpdatedGraphWrapper updatedGraph1 = updatedGraphResponseWrapper.getUpdatedGraphs()
                .get(0);
        Assert.assertEquals(updatedGraph1.isFullGraph(), true);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesIsFullGraphFalse() {
        UpdatedGraphWrapper updatedGraph2 = updatedGraphResponseWrapper.getUpdatedGraphs()
                .get(1);
        Assert.assertEquals(updatedGraph2.isFullGraph(), false);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesAddedNodesList() {
        UpdatedGraphWrapper updatedGraph1 = updatedGraphResponseWrapper.getUpdatedGraphs()
                .get(0);
        //        Assert.assertEquals(updatedGraph1.addedNodes.size(), 2);
        Assert.assertEquals(updatedGraph1.getAddedNodes().size(), 0);
    }

    @Test
    public void testUpdatedGraphWrapperCreationSavesDeletedNodesList() {
        UpdatedGraphWrapper updatedGraph2 = updatedGraphResponseWrapper.getUpdatedGraphs()
                .get(1);
        //        Assert.assertEquals(updatedGraph2.deletedNodes.size(), 2);
        Assert.assertEquals(updatedGraph2.getDeletedNodes().size(), 0);
    }

}
