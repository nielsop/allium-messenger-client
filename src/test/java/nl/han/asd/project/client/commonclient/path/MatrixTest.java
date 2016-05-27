package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.matrix.GraphMatrix;
import org.junit.Test;

/**
 * Created by BILLPOORTS on 25-5-2016.
 */
public class MatrixTest {

    @Test
    public void testMethod1() {
        Graph graph = new Graph();

        Node nodeA = new Node("A", "127.0.0.1", 1001, new byte[] { 0x00 });
        Node nodeB = new Node("B", "127.0.0.1", 1002, new byte[] { 0x00 });
        Node nodeC = new Node("C", "127.0.0.1", 1003, new byte[] { 0x00 });
        Node nodeE = new Node("E", "127.0.0.1", 1004, new byte[] { 0x00 });
        Node nodeF = new Node("F", "127.0.0.1", 1005, new byte[] { 0x00 });

        //nodeA.addEdge(nodeF, 10);
        nodeA.addEdge(nodeB, 20);
        nodeA.addEdge(nodeE, 10);
        nodeB.addEdge(nodeC, 50);
        nodeB.addEdge(nodeF, 110);
        nodeC.addEdge(nodeE, 5);
        nodeC.addEdge(nodeF, 25);
        nodeE.addEdge(nodeF, 51);

        graph.addNodeVertex(nodeA);
        graph.addNodeVertex(nodeB);
        graph.addNodeVertex(nodeC);
        graph.addNodeVertex(nodeE);
        graph.addNodeVertex(nodeF);

        GraphMatrix graphMatrix = new GraphMatrix(graph.getGraphMap());
        graphMatrix.fillMatrix();

        System.out.print(graphMatrix.toPrintableString());

        System.out.print("\n\n --- MULTIPLYING --- \n\n");

        graphMatrix.calculate();

        System.out.print(graphMatrix.toPrintableString());
    }

    @Test
    public void testMethod2() {
        Graph graph = new Graph();

        Node nodeOne = new Node("1", "127.0.0.1", 1001, new byte[] { 0x00 });
        Node nodeTwo = new Node("2", "127.0.0.1", 1002, new byte[] { 0x00 });
        Node nodeThree = new Node("3", "127.0.0.1", 1003, new byte[] { 0x00 });
        Node nodeFour = new Node("4", "127.0.0.1", 1004, new byte[] { 0x00 });

        nodeOne.addEdge(nodeTwo, 2);
        nodeOne.addEdge(nodeFour, 1);
        nodeTwo.addEdge(nodeThree, 4);
        nodeFour.addEdge(nodeThree, 6);

        graph.addNodeVertex(nodeOne);
        graph.addNodeVertex(nodeTwo);
        graph.addNodeVertex(nodeThree);
        graph.addNodeVertex(nodeFour);

        GraphMatrix graphMatrix = new GraphMatrix(graph.getGraphMap());
        graphMatrix.fillMatrix();

        System.out.print(graphMatrix.toPrintableString());

        System.out.print("\n\n --- MULTIPLYING (Step = 2) --- \n\n");

        graphMatrix.calculate();

        System.out.print(graphMatrix.toPrintableString());

        System.out.print("\n\n --- MULTIPLYING (Step = 3) --- \n\n");

        graphMatrix.calculate();

        System.out.print(graphMatrix.toPrintableString());

        System.out.print("\n\n --- MULTIPLYING (Step = 4) --- \n\n");

        graphMatrix.calculate();

        System.out.print(graphMatrix.toPrintableString());
    }
}
