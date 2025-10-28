package algorithms;

import models.Graph;
import models.Node;
import models.Edge;

import java.util.*;

/**
 * Algorithme de Bellman-Ford
 * Permet de calculer les plus courts chemins depuis une source unique,
 * même si le graphe contient des arêtes de poids négatif.
 */
public class BellmanFord {

    /**
     * Calcule les distances minimales depuis un nœud source vers tous les autres.
     *
     * @param graph   Le graphe à explorer
     * @param startId L'identifiant du nœud de départ
     * @return Une map (clé = Node, valeur = distance minimale)
     * @throws IllegalArgumentException si un cycle négatif est détecté
     */
    public static Map<Node, Double> shortestPaths(Graph graph, String startId) {
        Node start = graph.getNode(startId);
        if (start == null) {
            throw new IllegalArgumentException("Nœud de départ introuvable : " + startId);
        }

        // Initialisation des distances
        Map<Node, Double> distance = new HashMap<>();
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
                }
            }
        }

        // Étape 2 : détection de cycle négatif
        for (Edge edge : edges) {
            Node u = edge.getSource();
            Node v = edge.getTarget();
            if (distance.get(u) + edge.getWeight() < distance.get(v)) {
                throw new IllegalArgumentException("Cycle négatif détecté dans le graphe !");
            }
        }

        return distance;
    }
}

