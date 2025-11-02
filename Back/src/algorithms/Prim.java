package algorithms;

import models.*;
import java.util.*;

public class Prim {
    /*
     * Implémente l'algorithme de Prim pour trouver l'Arbre Couvrant
     * de Poids Minimum (ACPM).
     *
     * L'algorithme démarre d'un nœud source et construit l'arbre
     * en ajoutant l'arête la moins chère qui connecte un nœud de l'arbre
     * à un nœud hors de l'arbre, jusqu'à ce que tous les nœuds soient inclus.
     *
     * @param graph       Le graphe sur lequel chercher.
     * @param startNodeId L'ID de la ville où commencer la construction de l'arbre.
     * @return Une carte (Map) contenant :
     * - "edges" (List<Edge>): La liste des arêtes de l'ACPM.
     * - "totalWeight" (Double): Le coût total de l'ACPM.
     */
    public static Map<String, Object> prim(Graph graph, String startNodeId) {
        List<Edge> mstEdges = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        // Une file de priorité pour stocker les arêtes, triées par poids (croissant)
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        double totalWeight = 0;

        Node startNode = graph.getNode(startNodeId);
        if (startNode == null) {
            System.err.println("Nœud de départ " + startNodeId + " introuvable !");
            return Collections.emptyMap();
        }

        // 1. Commencer avec le premier nœud
        addNodeToMST(startNode, visited, edgeQueue);

        // 2. Boucler jusqu'à ce que la file soit vide ou que tous les nœuds soient visités
        while (!edgeQueue.isEmpty() && visited.size() < graph.getNodes().size()) {
            // Obtenir l'arête la moins chère de la file
            Edge minEdge = edgeQueue.poll();
            Node target = minEdge.getTarget();

            // 3. Vérifier si le nœud cible est déjà dans l'ACPM
            if (visited.contains(target)) {
                // Cette arête mène à un nœud déjà visité, on l'ignore (pour éviter un cycle)
                continue;
            }

            // 4. C'est une nouvelle arête valide. L'ajouter à l'ACPM.
            mstEdges.add(minEdge);
            totalWeight += minEdge.getWeight();

            // 5. Ajouter le nœud nouvellement atteint (target) à l'ACPM
            addNodeToMST(target, visited, edgeQueue);
        }

        // Retourner les résultats
        Map<String, Object> result = new HashMap<>();
        result.put("edges", mstEdges);
        result.put("totalWeight", totalWeight);
        return result;
    }

    /*
     * Méthode d'aide pour ajouter un nœud à l'ensemble des nœuds visités (ACPM)
     * et ajouter toutes ses arêtes sortantes à la file de priorité.
     */
    private static void addNodeToMST(Node node, Set<Node> visited, PriorityQueue<Edge> edgeQueue) {
        visited.add(node);
        // Ajouter toutes les arêtes de ce nœud à la file
        // La file les triera automatiquement par poids
        for (Edge edge : node.getEdges()) {
            // On ajoute seulement les arêtes pointant vers un nœud non visité
            // (C'est une optimisation; la vérification dans la boucle principale est critique)
            if (!visited.contains(edge.getTarget())) {
                edgeQueue.add(edge);
            }
        }
    }
}