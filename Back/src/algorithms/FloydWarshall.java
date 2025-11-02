package algorithms;

import java.util.*;
import models.*;

public class FloydWarshall {
    /**
     * Implémente l'algorithme de Floyd-Warshall.
     *
     * Cet algorithme trouve les plus courts chemins entre TOUTES les paires
     * de nœuds dans un graphe pondéré. Il gère les poids négatifs (mais
     * lèvera une exception s'il y a un cycle négatif, bien que cette
     * implémentation ne le détecte pas explicitement).
     *
     * @param graph Le graphe à traiter.
     * @return Une Map (représentant une matrice) où les clés sont les ID des sources,
     * et les valeurs sont des Maps (ID de destination -> coût du plus court chemin).
     */
    public static Map<String, Map<String, Double>> floydWarshall(Graph graph) {
        // Cette map contiendra notre matrice de distances : dist.get(source).get(target)
        Map<String, Map<String, Double>> dist = new HashMap<>();

        // Obtenir tous les ID de nœuds dans un ordre fixe
        List<String> nodeIds = new ArrayList<>();
        for (Node n : graph.getNodes()) {
            nodeIds.add(n.getId());
        }
        Collections.sort(nodeIds); // Assure un ordre cohérent

        // 1. Initialiser la matrice de distances
        for (String id1 : nodeIds) {
            Map<String, Double> innerMap = new HashMap<>();
            for (String id2 : nodeIds) {
                if (id1.equals(id2)) {
                    innerMap.put(id2, 0.0); // Distance à soi-même = 0
                } else {
                    innerMap.put(id2, Double.POSITIVE_INFINITY); // Aucun chemin connu
                }
            }
            dist.put(id1, innerMap);
        }

        // 2. Remplir la matrice avec les poids des arêtes directes
        for (Node sourceNode : graph.getNodes()) {
            String sourceId = sourceNode.getId();
            for (Edge edge : sourceNode.getEdges()) {
                String targetId = edge.getTarget().getId();
                // On utilise getWeight() directement
                dist.get(sourceId).put(targetId, edge.getWeight());
            }
        }

        // 3. Cœur de l'algorithme Floyd-Warshall
        // Itérer sur tous les nœuds 'k' comme intermédiaires potentiels
        for (String k : nodeIds) {
            // Pour chaque nœud source 'i'
            for (String i : nodeIds) {
                // Pour chaque nœud de destination 'j'
                for (String j : nodeIds) {
                    double distIK = dist.get(i).get(k);
                    double distKJ = dist.get(k).get(j);
                    double distIJ = dist.get(i).get(j);

                    // Si le chemin de i à j en passant par k est plus court
                    if (distIK + distKJ < distIJ) {
                        // Mettre à jour le plus court chemin
                        dist.get(i).put(j, distIK + distKJ);
                    }
                }
            }
        }

        return dist;
    }
}