package algorithms;

import models.Edge;
import models.Graph;
import models.Node;

import java.util.*;

public class  BFS {

    /**
     * Effectue un parcours en largeur (BFS) à partir d'un nœud de départ.
     *
     * Cet algorithme explore le graphe niveau par niveau et retourne à la fois
     * l'ordre dans lequel les nœuds ont été visités et les arêtes qui forment
     * l'arbre couvrant BFS (les arêtes utilisées pour découvrir de nouveaux nœuds).
     *
     * @param graph       Le graphe sur lequel effectuer le parcours.
     * @param startNodeId L'identifiant du nœud de départ.
     *
     * @return Une Map contenant deux entrées :
     * - "visitOrder" (List<Node>): L'ordre de visite des nœuds pour l'animation.
     * - "treeEdges" (List<Edge>): Les arêtes de l'arbre couvrant BFS.
     *
     * Retourne une Map vide si le nœud de départ n'est pas trouvé.
     */
    public static Map<String, Object> bfs(Graph graph, String startNodeId) {
        List<Node> visitedOrder = new ArrayList<>();
        List<Edge> treeEdges = new ArrayList<>();

        Set<Node> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

        Map<String, Object> results = new HashMap<>();
        results.put("visitOrder", visitedOrder);
        results.put("treeEdges", treeEdges);

        Node startNode = graph.getNode(startNodeId);
        if (startNode == null) {
            System.err.println("Start node " + startNodeId + " not found!");
            return results;
        }

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            visitedOrder.add(currentNode);

            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getTarget();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);

                    // Cette arête a été utilisée pour découvrir un nouveau nœud
                    treeEdges.add(edge); // <-- NOUVEAU
                }
            }
        }
        return results;
    }
}

