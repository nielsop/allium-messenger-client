package nl.han.asd.project.client.commonclient.path;

import nl.han.asd.project.client.commonclient.graph.Graph;
import nl.han.asd.project.client.commonclient.graph.Node;
import nl.han.asd.project.client.commonclient.path.algorithm.AStar;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BILLPOORTS on 19-5-2016.
 */
public class AStarTest {

    @Test
    public void buildGraph() {
        Graph graph = new Graph();

        Node nodeA = new Node("A", "127.0.0.1", 1001, new byte[] { 0x00});
        Node nodeB = new Node("B", "127.0.0.1", 1002, new byte[] { 0x00});
        Node nodeC = new Node("C", "127.0.0.1", 1003, new byte[] { 0x00});
        Node nodeE = new Node("E", "127.0.0.1", 1004, new byte[] { 0x00});
        Node nodeF = new Node("F", "127.0.0.1", 1005, new byte[] { 0x00});

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

        AStar astar = new AStar(graph);

        List<String> path = astar.aStar("A", "F");

    }

    @Test
    public void main() {
        Map<String, Map<String, Double>> hueristic = new HashMap<String, Map<String, Double>>();
        // map for A
        Map<String, Double> mapA = new HashMap<String, Double>();
        mapA.put("A",  0.0);
        mapA.put("B", 10.0);
        mapA.put("C", 20.0);
        mapA.put("E", 100.0);
        mapA.put("F", 110.0);


        // map for B
        Map<String, Double> mapB = new HashMap<String, Double>();
        mapB.put("A", 10.0);
        mapB.put("B",  0.0);
        mapB.put("C", 10.0);
        mapB.put("E", 25.0);
        mapB.put("F", 40.0);


        // map for C
        Map<String, Double> mapC = new HashMap<String, Double>();
        mapC.put("A", 20.0);
        mapC.put("B", 10.0);
        mapC.put("C",  0.0);
        mapC.put("E", 10.0);
        mapC.put("F", 30.0);


        // map for X
        Map<String, Double> mapX = new HashMap<String, Double>();
        mapX.put("A", 100.0);
        mapX.put("B", 25.0);
        mapX.put("C", 10.0);
        mapX.put("E",  0.0);
        mapX.put("F", 10.0);

        // map for X
        Map<String, Double> mapZ = new HashMap<String, Double>();
        mapZ.put("A", 110.0);
        mapZ.put("B",  40.0);
        mapZ.put("C",  30.0);
        mapZ.put("E", 10.0);
        mapZ.put("F",  0.0);

        hueristic.put("A", mapA);
        hueristic.put("B", mapB);
        hueristic.put("C", mapC);
        hueristic.put("E", mapX);
        hueristic.put("F", mapZ);

        GraphAStar<String> graph = new GraphAStar<String>(hueristic);
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("E");
        graph.addNode("F");

        graph.addEdge("A", "B",  20);
        graph.addEdge("A", "E", 10);
        graph.addEdge("B", "C", 50);
        graph.addEdge("C", "E", 5);
        graph.addEdge("C", "F", 25);
        graph.addEdge("E", "F", 51);

        nl.han.asd.project.client.commonclient.path.AStar<String> aStar = new nl.han.asd.project.client.commonclient.path.AStar<String>(graph);

        List<String> path = aStar.astar("A", "F");
    }

}
