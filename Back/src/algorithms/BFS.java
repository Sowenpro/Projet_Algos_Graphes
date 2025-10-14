package algorithms;

import models.Graph;
import models.Node;

import java.util.*;

public class BFS {

    /**
     * Effectue un parcours en largeur à partir d'un nœud source.
     *
     * @param graph   Le graphe à parcourir
     * @param startId L'identifiant du nœud de départ
     * @return Une liste des nœuds visités dans l'ordre de parcours
     */
    public static List<Node> traverse(Graph graph, String startId) {
        Node start = graph.getNode(startId);
        if (start == null) return Collections.emptyList();

        List<Node> visitedOrder = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedOrder.add(current);

            for (var edge : current.getEdges()) {
                Node neighbor = edge.getTarget();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visitedOrder;
    }
}

