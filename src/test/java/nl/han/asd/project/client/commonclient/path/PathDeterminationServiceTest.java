package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.store.Contact;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Julius on 15/04/16.
 */
public class PathDeterminationServiceTest {
    private PathDeterminationService pathDeterminationService;
    private Contact contact;
    @Before
    public void setUp() throws Exception {
        pathDeterminationService = new PathDeterminationService();
        contact = new Contact("Username");
        contact.setConnectedNodes(new Node[]{new Node(),new Node(),new Node(),new Node(),new Node()});
    }


    /*
    Comparing self created path with a pathDeterminationService generated path
     */
    @Test
    public void whenMiniumHops() {
        //Node[] selfMadePath = {new Node(),new Node(),new Node()};

        int minimunNodes = 3;
        Node[] generatePath = pathDeterminationService.getPath(3, contact);

        Assert.assertEquals(minimunNodes, generatePath.length);
    }

    /*
Checking if generatedPath contains Node objects
 */
    @Test
    public void checkIfGeneratedPathContainsNodes() {
        Node[] selfMadePath = {new Node(),new Node(),new Node()};

        Node[] generatePath = pathDeterminationService.getPath(3, contact);

        for(int i = 0; i < selfMadePath.length; i++){
            Assert.assertEquals(selfMadePath[i], generatePath[i]);
        }
    }

    /*
Checking if error is trown when miniumHops is negative number
*/
    @Test(expected = IllegalArgumentException.class)
    public void whenMinimunHopsIsNegativeThrowError() {
        Node[] generatePath = pathDeterminationService.getPath(-1, contact);
    }

    /*
    Check if the first Node in the generated path is a ConnectedNode from the host client.
     */
    @Test
    public void firstNodeInPathIsAConnectedNodeFromHostClient(){
        Node[] generatePath = pathDeterminationService.getPath(3,contact);
        Node[] contactConnectedNodes = contact.getConnectedNodes();
        Assert.assertTrue(inArray(generatePath[0], contactConnectedNodes));
    }

    @Test
    public void clientHostConnectedNodesAreUpdatedIfLastUpdateIsExpired(){

    }

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