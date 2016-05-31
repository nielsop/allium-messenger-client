package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.master.IGetClientGroup;
import nl.han.asd.project.client.commonclient.master.IGetUpdatedGraph;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

/**
 * Created by Julius on 15/04/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PathDeterminationServiceTest {
    @Mock
    IGetUpdatedGraph updatedGraphMock;

    @Mock
    IGetClientGroup clientGroupMock;
    @InjectMocks
    private PathDeterminationService pathDeterminationService;
    private Contact contact;

    @Before
    public void setUp() throws Exception {
        contact = new Contact("Username", "1234".getBytes());
        contact.setConnectedNodes(new Node[]{new Node("NODE_ID_1", "192.168.2.8", 1234, "123456789".getBytes()),
                new Node("NODE_ID_2", "192.168.2.9", 1234, "123456789".getBytes()),
                new Node("NODE_ID_3", "192.168.2.10", 1234, "123456789".getBytes())});
    }

    /*
    Comparing self created path with a pathDeterminationService generated path
     */
    @Test
    public void whenMiniumHops() {
        //Node[] selfMadePath = {new Node(),new Node(),new Node()};

        int minimunNodes = 3;
        List<Node> generatePath = pathDeterminationService.getPath(3, contact);

        Assert.assertEquals(minimunNodes, generatePath.size());
    }

    /*
    Checking if generatedPath contains Node objects
    */
    @Test
    public void checkIfGeneratedPathContainsNodes() {
        Node[] selfMadePath = {new Node("NODE_ID_1", "192.168.2.8", 1234, "123456789".getBytes()),
                new Node("NODE_ID_2", "192.168.2.9", 1234, "123456789".getBytes()),
                new Node("NODE_ID_3", "192.168.2.10", 1234, "123456789".getBytes())};

        List<Node> generatePath = pathDeterminationService.getPath(3, contact);

        for (int i = 0; i < selfMadePath.length; i++) {
            Assert.assertEquals(selfMadePath[i], generatePath.get(i));
        }
    }

    /*
    Checking if error is trown when miniumHops is negative number
    */
    @Test(expected = IllegalArgumentException.class)
    public void whenMinimunHopsIsNegativeThrowError() {
        List<Node> generatePath = pathDeterminationService.getPath(-1, contact);
    }

    /*
    Check if the first Node in the generated path is a ConnectedNode from the host client.
     */
    @Test
    public void firstNodeInPathIsAConnectedNodeFromHostClient() {
        List<Node> generatePath = pathDeterminationService.getPath(3, contact);
        Node[] contactConnectedNodes = new Node[0];
        try {
            contactConnectedNodes = contact.getConnectedNodes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(inArray(generatePath.get(0), contactConnectedNodes));
    }

    //    @Test
    //    public void clientHostConnectedNodesAreUpdatedIfLastUpdateIsExpired() {
    //        throw new NotImplementedException();
    //    }

    private boolean inArray(Node needle, Node[] haystack) {
        for (Node n : haystack) {
            if (n == needle) {
                return true;
            }
        }
        return false;
    }

    @After
    public void tearDown() throws Exception {

    }
}
