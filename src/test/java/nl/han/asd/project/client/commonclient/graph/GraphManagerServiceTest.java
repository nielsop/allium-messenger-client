package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.client.commonclient.CommonclientModule;
import nl.han.asd.project.client.commonclient.master.MasterGateway;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mock;

/**
 * Created by Julius on 25/04/16.
 */
public class GraphManagerServiceTest {

    private GraphManagerService graphManagerService;

    @Mock
    private MasterGateway masterGateway;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new CommonclientModule());
        graphManagerService = new GraphManagerService(new MasterGateway(injector.getInstance(IEncryptionService.class)));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckGraphVersion() throws Exception {
        graphManagerService.processGraphUpdates();
    }
}