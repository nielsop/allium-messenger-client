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

       // System.out.print(graphMatrix.toPrintableString());

        System.out.print("\n\n --- MULTIPLYING --- \n\n");

        graphMatrix.calculate();

       // System.out.print(graphMatrix.toPrintableString());
    }

    @Test
    public void testMethod2() {
        Graph graph = new Graph();

        Node nodeA = new Node("A", "127.0.0.1", 1001, new byte[] { 0x00 });
        Node nodeB = new Node("B", "127.0.0.1", 1002, new byte[] { 0x00 });
        Node nodeC = new Node("C", "127.0.0.1", 1003, new byte[] { 0x00 });
        Node nodeD = new Node("D", "127.0.0.1", 1004, new byte[] { 0x00 });
        Node nodeE = new Node("E", "127.0.0.1", 1005, new byte[] { 0x00 });
        Node nodeF = new Node("F", "127.0.0.1", 1006, new byte[] { 0x00 });

        nodeA.addEdge(nodeE, 8);
        nodeA.addEdge(nodeF, 4);
        nodeE.addEdge(nodeC, 3);
        nodeC.addEdge(nodeD, 1);
        nodeC.addEdge(nodeB, 10);
        nodeF.addEdge(nodeB, 2);
        nodeF.addEdge(nodeD, 3);

        graph.addNodeVertex(nodeA);
        graph.addNodeVertex(nodeB);
        graph.addNodeVertex(nodeC);
        graph.addNodeVertex(nodeD);
        graph.addNodeVertex(nodeE);
        graph.addNodeVertex(nodeF);

        GraphMatrix graphMatrix = new GraphMatrix(graph.getGraphMap());
        graphMatrix.fillMatrix();


        graphMatrix.calculate();

        for(int i = 0; i <= 3; i++)
        {
            System.out.print(String.format("\n\n --- MULTIPLYING (Step = %d) --- \n\n", i + 1));
            System.out.print(graphMatrix.toPrintableString(i));
        }
    }
}
