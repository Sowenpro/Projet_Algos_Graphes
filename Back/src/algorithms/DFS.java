package algorithms;


import models.*;
import java.util.*;

public class DFS {

    /**
     * Effectue un parcours en profondeur (DFS) à partir d'un nœud de départ.
     *
     * Cet algorithme explore le graphe en suivant un chemin aussi loin que possible
     * avant de revenir en arrière. Il retourne à la fois l'ordre dans lequel
     * les nœuds ont été visités et les arêtes qui forment l'arbre couvrant DFS.
     *
     * @param graph       Le graphe sur lequel effectuer le parcours.
     * @param startNodeId L'identifiant du nœud de départ.
     *
     * @return Une Map contenant deux entrées :
     * - "visitOrder" (List<Node>): L'ordre de visite des nœuds pour l'animation.
     * - "treeEdges" (List<Edge>): Les arêtes de l'arbre couvrant DFS.
     *
     * Retourne une Map vide si le nœud de départ n'est pas trouvé.
     */
    public static Map<String, Object> dfs(Graph graph, String startNodeId) {
        List<Node> visitedOrder = new ArrayList<>();
        List<Edge> treeEdges = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        Map<String, Object> results = new HashMap<>();
        results.put("visitOrder", visitedOrder);
        results.put("treeEdges", treeEdges);

        Node startNode = graph.getNode(startNodeId);
        if (startNode == null) {
            System.err.println("Start node " + startNodeId + " not found!");
            return results;
        }

        // Appeler la nouvelle méthode récursive
        dfsRecursive(startNode, visited, visitedOrder, treeEdges);

        return results;
    }

    /**
     * Méthode récursive privée pour le parcours DFS,
     * qui construit aussi l'arbre couvrant.
     */
    private static void dfsRecursive(Node currentNode, Set<Node> visited, List<Node> visitedOrder, List<Edge> treeEdges) {
        visited.add(currentNode);
        visitedOrder.add(currentNode);

        for (Edge edge : currentNode.getEdges()) {
            Node neighbor = edge.getTarget();
            if (!visited.contains(neighbor)) {
                // Cette arête est utilisée pour découvrir un nouveau nœud
                treeEdges.add(edge); // <-- NOUVEAU
                // Récursion
                dfsRecursive(neighbor, visited, visitedOrder, treeEdges);
            }
        }
    }
}
