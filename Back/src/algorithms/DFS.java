package algorithms;

import models.*;
import java.util.*;


public class DFS {

    public static List<Node> dfs(Graph graph, String startNodeId) {
        List<Node> visitedOrder = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        Node startNode = graph.getNode(startNodeId);

        if (startNode == null) {
            System.err.println("Start node " + startNodeId + " not found!");
            return visitedOrder;
        }

        dfsRecursive(startNode, visited, visitedOrder);
        return visitedOrder;
    }

    private static void dfsRecursive(Node currentNode, Set<Node> visited, List<Node> visitedOrder) {
        visited.add(currentNode);
        visitedOrder.add(currentNode);

        for (Edge edge : currentNode.getEdges()) {
            Node neighbor = edge.getTarget();
            if (!visited.contains(neighbor)) {
                dfsRecursive(neighbor, visited, visitedOrder);
            }
        }
    }

}
