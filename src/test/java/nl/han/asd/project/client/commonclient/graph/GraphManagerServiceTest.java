package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.*;
import org.junit.rules.Timeout;
import org.mockito.Mock;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Julius on 25/04/16.
 */
public class GraphManagerServiceTest {

    private GraphManagerService graphManagerService;
private CloudHost master;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    @Before
    public void setUp() throws Exception {
        master = CloudHostFactory.getCloudHost("master");
        master.setup();
        Injector injector = Guice.createInjector(new EncryptionModule());
        while (true) {
            try {
                new Socket(master.getHostName(), master.getPort(1337));
                break;
            } catch (IOException e) {
                System.out.println("Trying again in 2 seconds");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MasterGateway gateway = new MasterGateway(master.getHostName(), master.getPort(1337), injector.getInstance(
                IEncryptionService.class));
        graphManagerService = new GraphManagerService(gateway);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckGraphVersion() throws Exception {
//        graphManagerService.processGraphUpdates();
        Assert.assertEquals(1, 1); //TODO: testcase afmaken
    }
}