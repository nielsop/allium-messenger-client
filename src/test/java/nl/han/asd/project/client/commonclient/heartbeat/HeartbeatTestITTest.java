package nl.han.asd.project.client.commonclient.heartbeat;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientHeartbeat;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientHeartbeat.Builder;

public class HeartbeatTestITTest {

    private Properties properties = new Properties();

    private IHeartbeat heartbeatMock;

    private ThreadedHeartbeatService threadedHeartbeat;

    @Before
    public void setup() {
        heartbeatMock = mock(IHeartbeat.class);
        threadedHeartbeat = new ThreadedHeartbeatService(properties, heartbeatMock);
    }

    @Test
    public void testHearbeat() throws Exception {
        properties.setProperty("heartbeat-delay", Integer.toString(1));

        CurrentUser contact = new CurrentUser("username", "key".getBytes(), "secret hash");

        threadedHeartbeat.startHeartbeatFor(contact);

        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        Builder clientHeartbeatBuilder = ClientHeartbeat.newBuilder();
        clientHeartbeatBuilder.setUsername(contact.getCurrentUserAsContact().getUsername());
        clientHeartbeatBuilder.setSecretHash(contact.getSecretHash());
        verify(heartbeatMock, atMost(2)).sendHeartbeat(eq(clientHeartbeatBuilder.build()));
        verify(heartbeatMock, atLeast(1)).sendHeartbeat(eq(clientHeartbeatBuilder.build()));

        assertTrue(threadedHeartbeat.stopHeartbeatFor(contact));
    }
}
