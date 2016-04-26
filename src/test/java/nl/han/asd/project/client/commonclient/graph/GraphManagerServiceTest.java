package nl.han.asd.project.client.commonclient.graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Julius on 25/04/16.
 */
public class GraphManagerServiceTest {

    private GraphManagerService graphManagerService;

    @Before
    public void setUp() throws Exception {
        graphManagerService = new GraphManagerService("10.182.5.216",1337);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckGraphVersion() throws Exception {
        graphManagerService.checkGraphVersion(3);
    }
}