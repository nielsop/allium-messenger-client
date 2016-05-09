package nl.han.asd.project.client.commonclient.master.wrapper;

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
public class LoginResponseWrapperTest {
    /**
     * this.nodeList = nodeList;
     * this.secretHash = secretHash;
     * this.status = status;
     */

    private static List<HanRoutingProtocol.Node> nodeList;

    @BeforeClass
    public static void setupTestClass() {
        nodeList = new ArrayList<>();
        nodeList.add(HanRoutingProtocol.Node.newBuilder().setId("Node-1").setIPaddress("127.0.0.1").setPort(1337).setPublicKey("0x00").build());
        nodeList.add(HanRoutingProtocol.Node.newBuilder().setId("Node-2").setIPaddress("127.0.0.2").setPort(1337).setPublicKey("0x00").build());
        nodeList.add(HanRoutingProtocol.Node.newBuilder().setId("Node-3").setIPaddress("127.0.0.3").setPort(1337).setPublicKey("0x00").build());
    }

    @Test
    public void testLoginWrapperCreationSavesNodes() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123", HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.nodeList.size(), 3);
    }

    @Test
    public void testLoginWrapperCreationSavesSecretHash() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123", HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.secretHash, "SUPER-SECRET-HASH-123");
    }

    @Test
    public void testLoginWrapperCreationSavesStatus() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123", HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.status, HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

}