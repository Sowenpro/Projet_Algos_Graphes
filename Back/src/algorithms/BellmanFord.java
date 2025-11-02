package algorithms;

import models.Graph;
import models.Node;
import models.Edge;

import java.util.*;

/**
 * Algorithme de Bellman-Ford
 */
public class BellmanFord {

    /**
     * Calcule les distances minimales depuis un nœud source vers tous les autres.
     *
     * @param graph   Le graphe à explorer
     * @param startId L'identifiant du nœud de départ
     * @return Une Map (String -> Object) contenant :
     * - "distances" (Map<Node, Double>)
     * - "predecessors" (Map<Node, Node>)
     * - "negativeCycle" (Boolean)
     */
    public static Map<String, Object> findShortestPaths(Graph graph, String startId) {
        Node start = graph.getNode(startId);

        // Map de résultats
        Map<String, Object> result = new HashMap<>();

        if (start == null) {
            throw new IllegalArgumentException("Nœud de départ introuvable : " + startId);
        }

        // Initialisation
        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>(); // <-- AJOUTÉ
        for (Node node : graph.getNodes()) {
            distance.put(node, Double.POSITIVE_INFINITY);
        }
        distance.put(start, 0.0);

        int nbNodes = graph.getNodes().size();
        List<Edge> edges = graph.getAllEdges();

        // Étape 1 : relaxation des arêtes |V|-1 fois
        for (int i = 0; i < nbNodes - 1; i++) {
            for (Edge edge : edges) {
                Node u = edge.getSource();
                Node v = edge.getTarget();
                double newDist = distance.get(u) + edge.getWeight();

                if (distance.get(u) != Double.POSITIVE_INFINITY && newDist < distance.get(v)) {
                    distance.put(v, newDist);
                    predecessors.put(v, u); // <-- AJOUTÉ
                }
            }
        }

        // Étape 2 : détection de cycle négatif
        for (Edge edge : edges) {
            Node u = edge.getSource();
            Node v = edge.getTarget();
            if (distance.get(u) != Double.POSITIVE_INFINITY && distance.get(u) + edge.getWeight() < distance.get(v)) {
                // Cycle négatif détecté !
                result.put("negativeCycle", true); // <-- MODIFIÉ
                return result; // On retourne l'erreur
            }
        }

        // Pas de cycle, on retourne les résultats
        result.put("negativeCycle", false);
        result.put("distances", distance);
        result.put("predecessors", predecessors);
        return result;
    }
}