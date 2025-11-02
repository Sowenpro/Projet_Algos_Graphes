package algorithms;

import models.*;
import java.util.*;

public class Dijkstra {

    /**
     * Classe interne pour aider la file de priorité de Dijkstra.
     * Elle stocke un nœud et sa distance actuelle depuis le début.
     */
    private static class NodeEntry implements Comparable<NodeEntry> {
        Node node;
        double distance;

        NodeEntry(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeEntry other) {
            // Compare les distances pour que la PriorityQueue
            // retourne toujours le nœud avec la plus petite distance.
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * Implémente l'algorithme de Dijkstra pour trouver le plus court chemin
     * entre deux nœuds dans un graphe pondéré (avec poids positifs).
     *
     * @param graph       Le graphe sur lequel chercher.
     * @param startNodeId La ville de départ.
     * @param endNodeId   La ville d'arrivée.
     *
     * @return Une carte contenant deux entrées :
     * - "path" (List<Node>): Le chemin le plus court (liste de nœuds).
     * - "distance" (Double): La distance totale du chemin.
     *
     * Retourne une carte vide ou un chemin vide si les nœuds sont introuvables
     * ou si aucun chemin n'existe.
     */
    public static Map<String, Object> dijkstra(Graph graph, String startNodeId, String endNodeId) {
        Node startNode = graph.getNode(startNodeId);
        Node endNode = graph.getNode(endNodeId);

        if (startNode == null || endNode == null) {
            System.err.println("Nœud de départ ou d'arrivée introuvable !");
            return Collections.emptyMap();
        }

        // 1. Structures de données
        // Stocke la distance la plus courte trouvée jusqu'à présent pour chaque nœud
        Map<Node, Double> distances = new HashMap<>();
        // Stocke le "prédécesseur" de chaque nœud dans le chemin le plus court
        Map<Node, Node> predecessors = new HashMap<>();
        // File de priorité pour extraire le nœud non visité avec la plus petite distance
        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();
        // Ensemble des nœuds dont le chemin le plus court est déjà finalisé
        Set<Node> visited = new HashSet<>();

        // 2. Initialisation
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(startNode, 0.0); // La distance au départ est 0
        pq.add(new NodeEntry(startNode, 0.0));

        // 3. Boucle principale de Dijkstra
        while (!pq.isEmpty()) {
            NodeEntry currentEntry = pq.poll();
            Node currentNode = currentEntry.node;

            // Si ce nœud a déjà été finalisé, on l'ignore
            // (cela gère les entrées obsolètes dans la file)
            if (visited.contains(currentNode)) {
                continue;
            }

            // Finaliser ce nœud
            visited.add(currentNode);

            // Optimisation : si on a atteint la destination, on peut s'arrêter
            if (currentNode == endNode) {
                break;
            }

            // 4. "Relâcher" les voisins
            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getTarget();

                // Ne pas traiter les nœuds déjà finalisés
                if (visited.contains(neighbor)) {
                    continue;
                }

                double newDist = distances.get(currentNode) + edge.getWeight();

                // Si on a trouvé un chemin plus court vers ce voisin
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, currentNode);
                    // Ajouter le voisin à la file avec sa nouvelle distance
                    pq.add(new NodeEntry(neighbor, newDist));
                }
            }
        }

        // 5. Reconstruction du chemin
        List<Node> path = new ArrayList<>();
        Node step = endNode;

        // Si le nœud d'arrivée n'a pas de prédécesseur (et n'est pas le départ),
        // alors aucun chemin n'a été trouvé.
        if (predecessors.get(step) == null && step != startNode) {
            return Map.of("distance", Double.POSITIVE_INFINITY, "path", path); // Pas de chemin
        }

        // Remonter les prédécesseurs depuis l'arrivée
        while (step != null) {
            path.add(step);
            step = predecessors.get(step);
        }
        Collections.reverse(path); // Mettre le chemin dans le bon ordre (départ -> arrivée)

        // 6. Retourner le résultat
        Map<String, Object> result = new HashMap<>();
        result.put("path", path);
        result.put("distance", distances.get(endNode));
        return result;
    }
}
