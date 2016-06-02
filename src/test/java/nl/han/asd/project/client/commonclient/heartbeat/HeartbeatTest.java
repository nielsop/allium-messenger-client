package nl.han.asd.project.client.commonclient.heartbeat;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import nl.han.asd.project.client.commonclient.heartbeat.ThreadedHeartbeatService.HeartbeatSender;
import nl.han.asd.project.client.commonclient.master.IHeartbeat;
import nl.han.asd.project.client.commonclient.store.CurrentUser;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ThreadedHeartbeatService.class, HeartbeatSender.class })
public class HeartbeatTest {

    private Properties properties = new Properties();

    private IHeartbeat heartbeatMock;

    private ThreadedHeartbeatService threadedHeartbeat;

    @Before
    public void setup() {
        heartbeatMock = mock(IHeartbeat.class);
        threadedHeartbeat = new ThreadedHeartbeatService(properties, heartbeatMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProperties() throws Exception {
        new ThreadedHeartbeatService(null, heartbeatMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullHeartbeat() throws Exception {
        new ThreadedHeartbeatService(properties, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startheartbeatNullContact() throws Exception {
        threadedHeartbeat.startHeartbeatFor(null);
    }

    @Test
    public void startHeartbeatExistingContact() throws Exception {
        CurrentUser contact = new CurrentUser("username", "key".getBytes(), "secret hash");

        HeartbeatSender heartbeatSender = mock(HeartbeatSender.class);
        whenNew(HeartbeatSender.class).withArguments(eq(contact)).thenReturn(heartbeatSender);
        threadedHeartbeat.startHeartbeatFor(contact);

        verify(heartbeatSender).start();

        threadedHeartbeat.startHeartbeatFor(contact);
        verifyNew(HeartbeatSender.class, times(0)).withArguments(eq(HeartbeatSender.class));
    }

    @Test
    public void startHeartbeatNewContact() throws Exception {
        CurrentUser contact = new CurrentUser("username", "key".getBytes(), "secret hash");

        HeartbeatSender heartbeatSenderMock = mock(HeartbeatSender.class);
        whenNew(HeartbeatSender.class).withArguments(eq(contact)).thenReturn(heartbeatSenderMock);
        threadedHeartbeat.startHeartbeatFor(contact);

        verify(heartbeatSenderMock).start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopHeartbeatNullContact() throws Exception {
        threadedHeartbeat.startHeartbeatFor(null);
    }

    @Test
    public void stopHeartbeatNonScheduledUser() throws Exception {
        CurrentUser contact = new CurrentUser("username", "key".getBytes(), "secret hash");
        threadedHeartbeat.stopHeartbeatFor(contact);
    }

    @Test
    public void stopheartbeatScheduledUser() throws Exception {
        CurrentUser contact = new CurrentUser("username", "key".getBytes(), "secret hash");

        HeartbeatSender heartbeatSender = mock(HeartbeatSender.class);
        whenNew(HeartbeatSender.class).withArguments(eq(contact)).thenReturn(heartbeatSender);
        threadedHeartbeat.startHeartbeatFor(contact);

        verify(heartbeatSender).start();

        threadedHeartbeat.stopHeartbeatFor(contact);

        verify(heartbeatSender).interrupt();
        verify(heartbeatSender).join();
    }

}
