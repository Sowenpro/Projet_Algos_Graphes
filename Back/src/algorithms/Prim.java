package algorithms;

import models.*;
import java.util.*;

public class Prim {
    /**
     * Implements Prim's algorithm to find the Minimum Spanning Tree (MST).
     *
     * @param graph       The graph to search.
     * @param startNodeId The ID of the city to start building the tree from.
     * @return A map containing the list of edges in the MST and the total cost.
     */
    public static Map<String, Object> prim(Graph graph, String startNodeId) {
        List<Edge> mstEdges = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        // A priority queue to store edges, ordered by weight (ascending)
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        double totalWeight = 0;

        Node startNode = graph.getNode(startNodeId);
        if (startNode == null) {
            System.err.println("Start node " + startNodeId + " not found!");
            return Collections.emptyMap();
        }

        // 1. Start with the first node
        addNodeToMST(startNode, visited, edgeQueue);

        // 2. Loop until the queue is empty or all nodes are visited
        while (!edgeQueue.isEmpty() && visited.size() < graph.getNodes().size()) {
            // Get the cheapest edge from the queue
            Edge minEdge = edgeQueue.poll();
            Node target = minEdge.getTarget();

            // 3. Check if the target node is already in the MST
            if (visited.contains(target)) {
                // This edge leads to an already-visited node, so skip it to avoid a cycle
                continue;
            }

            // 4. This is a valid new edge. Add it to the MST.
            mstEdges.add(minEdge);
            totalWeight += minEdge.getWeight();

            // 5. Add the newly reached node (target) to the MST
            addNodeToMST(target, visited, edgeQueue);
        }

        // Return the results
        Map<String, Object> result = new HashMap<>();
        result.put("edges", mstEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }

    /**
     * Helper method to add a node to the MST's visited set and
     * add all its outgoing edges to the priority queue.
     */
    private static void addNodeToMST(Node node, Set<Node> visited, PriorityQueue<Edge> edgeQueue) {
        visited.add(node);
        // Add all edges from this node to the queue
        // The queue will automatically sort them by weight
        for (Edge edge : node.getEdges()) {
            // We only add edges pointing to an unvisited node
            // (This is an optimization; the main loop's check is what's critical)
            if (!visited.contains(edge.getTarget())) {
                edgeQueue.add(edge);
            }
        }
    }
}
