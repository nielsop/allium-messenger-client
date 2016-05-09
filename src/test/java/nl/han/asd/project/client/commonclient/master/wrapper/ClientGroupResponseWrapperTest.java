package nl.han.asd.project.client.commonclient.master.wrapper;

import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class ClientGroupResponseWrapperTest {

    private static List<HanRoutingProtocol.Client> clients;

    @BeforeClass
    public static void setupTestClass() {
        clients = new ArrayList<>();
        clients.add(HanRoutingProtocol.Client.newBuilder().setUsername("Nielsje41").setPublicKey("ABCDEF").build());
    }

    @Test
    public void testNewClientGroupResponse() {
        ClientGroupResponseWrapper responseWrapper = new ClientGroupResponseWrapper(clients);
        Assert.assertTrue(responseWrapper.clientGroup.size() > 0);
    }

}