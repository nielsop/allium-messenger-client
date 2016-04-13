package nl.han.onionmessenger.commonclient.master;

import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 13-4-2016
 */
public class MasterGatewayTest {

    private CloudHost itestHost;
    private MasterGateway masterGateway;

    @Test
    public void randomTest() {
        Hrp.
    }
    @Before
    public void before() throws UnknownHostException {
        itestHost = CloudHostFactory.getCloudHost("server");
        itestHost.setup();

        String host = itestHost.getHostName();
        int port = itestHost.getPort(31337);

        System.out.println(host + ", " + port);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        masterGateway = new MasterGateway(host, port);
    }

    @After
    public void tearDown() throws Exception {
        itestHost.teardown();
    }
}