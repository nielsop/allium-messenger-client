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
public class ClientGroupResponseWrapperTest {

    private static final byte[] EMPTY_PUBLICKEY_BYTES = new byte[]{0x00};
    private static List<HanRoutingProtocol.Client> clients;

    @BeforeClass
    public static void setupTestClass() {
        clients = new ArrayList<>();
        clients.add(HanRoutingProtocol.Client.newBuilder().setUsername("Nielsje41").setPublicKey(
                ByteString.copyFrom(EMPTY_PUBLICKEY_BYTES)).build());
    }

    @Test
    public void testNewClientGroupResponse() {
        ClientGroupResponseWrapper responseWrapper = new ClientGroupResponseWrapper(clients);
        Assert.assertTrue(responseWrapper.getClientGroup().size() > 0);
    }

}
