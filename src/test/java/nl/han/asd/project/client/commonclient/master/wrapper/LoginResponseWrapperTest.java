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
    private static final byte[] EMPTY_PUBLICKEY_BYTES = new byte[] { 0x00 };
    private static List<String> nodeList;

    @BeforeClass
    public static void setupTestClass() {
        nodeList = new ArrayList<>();

        nodeList.add("Node-1");
        nodeList.add("Node-2");
        nodeList.add("Node-3");

    }

    @Test
    public void testLoginWrapperCreationSavesNodes() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123",
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.getNodeList().size(), 3);
    }

    @Test
    public void testLoginWrapperCreationSavesSecretHash() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123",
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.getSecretHash(), "SUPER-SECRET-HASH-123");
    }

    @Test
    public void testLoginWrapperCreationSavesStatus() {
        final LoginResponseWrapper responseWrapper = new LoginResponseWrapper(nodeList, "SUPER-SECRET-HASH-123",
                HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
        Assert.assertEquals(responseWrapper.getStatus(), HanRoutingProtocol.ClientLoginResponse.Status.SUCCES);
    }

}
