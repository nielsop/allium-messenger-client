package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.*;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.Socket;

public class GraphManagerServiceTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);
    private GraphManagerService graphManagerService;
    private CloudHost master;

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
        MasterGateway gateway = new MasterGateway(injector.getInstance(IEncryptionService.class));
        gateway.setConnectionData(master.getHostName(), master.getPort(1337));
        graphManagerService = new GraphManagerService(gateway);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test @Ignore("To be implemented")
    public void testCheckGraphVersion() throws Exception {
        //        graphManagerService.processGraphUpdates();
        Assert.assertEquals(1, 1); //TODO: testcase afmaken
    }
}
