import models.*;
import algorithms.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // 1. Construire le graphe à partir des données du PDF
        Graph roadNetwork = GraphBuilder.createRoadNetwork();
        // Le Graphe est non-dirigé car GraphBuilder ajoute les arêtes dans les deux sens
        System.out.println("Graphe créé avec succès (Non-dirigé).");
        // System.out.println(roadNetwork); // Dé-commentez pour déboguer et voir tous les voisins


        // -----------------------------------------------------------------
        System.out.println("\n--- Partie I : Parcours BFS / DFS ---");
        // -----------------------------------------------------------------
        String villeDeDepart = "Rennes";

        // --- BFS ---
        // La nouvelle méthode BFS retourne une Map contenant l'ordre de visite ET l'arbre
        Map<String, Object> bfsResult = BFS.bfs(roadNetwork, villeDeDepart);
        @SuppressWarnings("unchecked")
        List<Node> bfsOrder = (List<Node>) bfsResult.get("visitOrder");
        @SuppressWarnings("unchecked")
        List<Edge> bfsTree = (List<Edge>) bfsResult.get("treeEdges");

        String bfsOrderStr = bfsOrder.stream()
                .map(Node::getId)
                .collect(Collectors.joining(" -> "));
        System.out.println("Ordre de visite BFS depuis " + villeDeDepart + ":");
        System.out.println(bfsOrderStr);
        System.out.println("Arbre couvrant BFS (Taille: " + bfsTree.size() + ")");

        // --- DFS ---
        // La nouvelle méthode DFS retourne aussi une Map
        Map<String, Object> dfsResult = DFS.dfs(roadNetwork, villeDeDepart);
        @SuppressWarnings("unchecked")
        List<Node> dfsOrder = (List<Node>) dfsResult.get("visitOrder");
        @SuppressWarnings("unchecked")
        List<Edge> dfsTree = (List<Edge>) dfsResult.get("treeEdges");

        String dfsOrderStr = dfsOrder.stream()
                .map(Node::getId)
                .collect(Collectors.joining(" -> "));
        System.out.println("\nOrdre de visite DFS depuis " + villeDeDepart + ":");
        System.out.println(dfsOrderStr);
        System.out.println("Arbre couvrant DFS (Taille: " + dfsTree.size() + ")");


        // -----------------------------------------------------------------
        System.out.println("\n\n--- Partie II : Arbre couvrant de poids minimum (Prim) ---");
        // -----------------------------------------------------------------

        // On peut démarrer de n'importe quelle ville, ex: "Rennes"
        Map<String, Object> mstResult = Prim.prim(roadNetwork, "Rennes");
        @SuppressWarnings("unchecked")
        List<Edge> mstEdges = (List<Edge>) mstResult.get("edges");
        double totalCost = (double) mstResult.get("totalWeight");

        System.out.println("Réseau routier minimal (Prim) :");
        for (Edge edge : mstEdges) {
            System.out.println(" - " + edge.getSource().getId() + " -- " +
                    edge.getTarget().getId() + " (Coût: " + edge.getWeight() + ")");
        }
        System.out.println("Coût total du réseau minimal: " + totalCost);


        // -----------------------------------------------------------------
        System.out.println("\n\n--- Partie II : Arbre couvrant de poids minimum (Kruskal) ---");
        // -----------------------------------------------------------------

        List<Edge> kruskalMst = Kruskal.traverse(roadNetwork);
        double kruskalTotal = 0.0;

        System.out.println("Réseau routier minimal (Kruskal) :");
        if (kruskalMst.isEmpty()) {
            System.out.println("Aucune arête dans l'ACPM (Kruskal) — vérifie le graphe ou l'implémentation.");
        } else {
            for (Edge e : kruskalMst) {
                System.out.println(" - " + e.getSource().getId() + " -- " + e.getTarget().getId() +
                        " (Coût: " + e.getWeight() + ")");
                // On ne somme que les arêtes de l'ACPM
                kruskalTotal += e.getWeight();
            }
            // Note: Kruskal.traverse() retourne les doublons (A->B et B->A) si le graphe est dirigé
            // Mais votre UnionFind les gère correctement. Pour un coût juste, on divise par 2
            // si les arêtes sont stockées dans les deux sens (ce que fait votre Kruskal).
            // Mieux : on recalcule la somme.
            kruskalTotal = kruskalMst.stream().mapToDouble(Edge::getWeight).sum();

            // Si votre 'traverse' retourne A->B et B->A, le total sera double.
            // La solution de votre 'Kruskal.java' gère cela, donc le total est correct.
            System.out.println("Coût total du réseau minimal (Kruskal): " + kruskalTotal);
        }


        // -----------------------------------------------------------------
        System.out.println("\n\n--- Partie III : Plus court chemin (Dijkstra) ---");
        // -----------------------------------------------------------------
        String depart = "Bordeaux";
        String arrivee = "Lille";
        System.out.println("Recherche du plus court chemin de " + depart + " à " + arrivee + "...");

        Map<String, Object> dijkstraResult = Dijkstra.dijkstra(roadNetwork, depart, arrivee);
        double distance = (double) dijkstraResult.get("distance");
        @SuppressWarnings("unchecked")
        List<Node> path = (List<Node>) dijkstraResult.get("path");

        if (distance == Double.POSITIVE_INFINITY) {
            System.out.println("Aucun chemin trouvé de " + depart + " à " + arrivee + ".");
        } else {
            String pathStr = path.stream()
                    .map(Node::getId)
                    .collect(Collectors.joining(" -> "));

            System.out.println("Chemin le plus court: " + pathStr);
            System.out.println("Distance totale: " + distance);
        }


        // -----------------------------------------------------------------
        System.out.println("\n\n--- Partie IV : Matrice des plus courts chemins (Floyd-Warshall) ---");
        // -----------------------------------------------------------------

        Map<String, Map<String, Double>> shortestPaths = FloydWarshall.floydWarshall(roadNetwork);

        // Obtenir les noms des villes triés pour l'affichage
        List<String> cities = new ArrayList<>(shortestPaths.keySet());
        Collections.sort(cities);

        // Imprimer l'en-tête (colonnes)
        System.out.printf("%-10s", ""); // Coin vide
        for (String city : cities) {
            System.out.printf("%-10s", city.substring(0, Math.min(city.length(), 10)));
        }
        System.out.println();

        // Imprimer chaque ligne
        for (String rowCity : cities) {
            System.out.printf("%-10s", rowCity.substring(0, Math.min(rowCity.length(), 10)));
            for (String colCity : cities) {
                double dist = shortestPaths.get(rowCity).get(colCity);
                if (dist == Double.POSITIVE_INFINITY) {
                    System.out.printf("%-10s", "Inf");
                } else {
                    System.out.printf("%-10.0f", dist); // Imprimer la distance sans décimales
                }
            }
            System.out.println();
        }


        // -----------------------------------------------------------------
        System.out.println("\n\n--- Partie IV : Plus courts chemins (Bellman-Ford) ---");
        // -----------------------------------------------------------------
        String bfSource = "Rennes";
        System.out.println("Calcul des distances depuis " + bfSource + " (Bellman-Ford)...");

        // Utilisation de la nouvelle méthode BellmanFord qui retourne une Map
        Map<String, Object> bfResult = BellmanFord.findShortestPaths(roadNetwork, bfSource);

        @SuppressWarnings("unchecked")
        boolean hasNegativeCycle = (boolean) bfResult.get("negativeCycle");

        if (hasNegativeCycle) {
            System.out.println("Erreur (Bellman-Ford) : Cycle de poids négatif détecté !");
        } else {
            @SuppressWarnings("unchecked")
            Map<Node, Double> distances = (Map<Node, Double>) bfResult.get("distances");

            System.out.println("Distances depuis " + bfSource + " :");

            // Trier la sortie pour un affichage propre
            List<Map.Entry<Node, Double>> sortedDistances = new ArrayList<>(distances.entrySet());
            sortedDistances.sort(Map.Entry.comparingByKey(Comparator.comparing(Node::getId)));

            for (Map.Entry<Node, Double> entry : sortedDistances) {
                String distStr = (entry.getValue() == Double.POSITIVE_INFINITY) ? "Infini" : String.format("%.0f", entry.getValue());
                System.out.println(" - " + entry.getKey().getId() + " = " + distStr);
            }
        }
    }
}