package algorithms;

import java.util.*;
import models.*;

public class FloydWarshall {
    /**
     * Implements the Floyd-Warshall algorithm to find all-pairs shortest paths.
     *
     * @param graph The graph to process.
     * @return A map (matrix) where keys are source IDs, and values are maps
     * of destination IDs to their shortest path cost.
     */
    public static Map<String, Map<String, Double>> floydWarshall(Graph graph) {
        // This map will hold our distance matrix: dist.get(source).get(target)
        Map<String, Map<String, Double>> dist = new HashMap<>();

        // Get all node IDs in a fixed, sorted order for iteration
        List<String> nodeIds = new ArrayList<>();
        for (Node n : graph.getNodes()) {
            nodeIds.add(n.getId());
        }
        Collections.sort(nodeIds); // Ensures consistent order

        // 1. Initialize the distance matrix
        for (String id1 : nodeIds) {
            Map<String, Double> innerMap = new HashMap<>();
            for (String id2 : nodeIds) {
                if (id1.equals(id2)) {
                    innerMap.put(id2, 0.0); // Distance to self is 0
                } else {
                    innerMap.put(id2, Double.POSITIVE_INFINITY); // No path known yet
                }
            }
            dist.put(id1, innerMap);
        }

        // 2. Populate the matrix with direct edge weights
        for (Node sourceNode : graph.getNodes()) {
            String sourceId = sourceNode.getId();
            for (Edge edge : sourceNode.getEdges()) {
                String targetId = edge.getTarget().getId();
                // We use getWeight() directly because our graph is now "undirected"
                // (has edges in both directions)
                dist.get(sourceId).put(targetId, edge.getWeight());
            }
        }

        // 3. The core Floyd-Warshall algorithm
        // Iterate through all nodes 'k' as potential intermediate nodes
        for (String k : nodeIds) {
            // For every source node 'i'
            for (String i : nodeIds) {
                // For every destination node 'j'
                for (String j : nodeIds) {
                    double distIK = dist.get(i).get(k);
                    double distKJ = dist.get(k).get(j);
                    double distIJ = dist.get(i).get(j);

                    // If the path from i to j through k is shorter
                    if (distIK + distKJ < distIJ) {
                        // Update the shortest path
                        dist.get(i).put(j, distIK + distKJ);
                    }
                }
            }
        }

        return dist;
    }
}
