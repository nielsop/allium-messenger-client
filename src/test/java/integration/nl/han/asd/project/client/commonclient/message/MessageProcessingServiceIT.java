package integration.nl.han.asd.project.client.commonclient.message;

import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.connection.ConnectionService;
import org.junit.After;
import org.junit.Before;

public class MessageProcessingServiceIT {
    private ConnectionService connectionService;
    private CloudHost master;

    @Before public void setup() {
        master = CloudHostFactory.getCloudHost("node");
        master.setup();

        //connectionService = new ConnectionService(new byte[] { 0x00, 0x00 });
    }

    @After public void after() {
        master.teardown();
    }
}
