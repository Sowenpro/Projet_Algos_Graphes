import models.*;
import algorithms.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // 1. Build the graph from the PDF data
        Graph roadNetwork = GraphBuilder.createRoadNetwork();
        System.out.println("Graph created successfully (Directed).");
        // System.out.println(roadNetwork); // Uncomment to debug and see all edges

        System.out.println("\n--- Part I: Parcours BFS / DFS ---");

        // Example: Give the visit order of reachable cities from Rennes
        String startCity = "Rennes";

        // --- BFS ---
        List<Node> bfsResult = BFS.traverse(roadNetwork, startCity);
        String bfsOrder = bfsResult.stream()
                .map(Node::getId)
                .collect(Collectors.joining(" -> "));
        System.out.println("BFS visit order from " + startCity + ":");
        System.out.println(bfsOrder);

        // --- DFS ---
        List<Node> dfsResult = DFS.dfs(roadNetwork, startCity);
        String dfsOrder = dfsResult.stream()
                .map(Node::getId)
                .collect(Collectors.joining(" -> "));
        System.out.println("\nDFS visit order from " + startCity + ":");
        System.out.println(dfsOrder);

        // --- Part II: Arbre couvrant de poids minimum (Prim) ---
        System.out.println("\n\n--- Part II: Arbre couvrant de poids minimum (Prim) ---");

        // We can start from any city, e.g., "Rennes"
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

        // --- Part II: Arbre couvrant de poids minimum (Kruskal) ---
        System.out.println("\n\n--- Part II: Arbre couvrant de poids minimum (Kruskal) ---");

        List<Edge> kruskalMst = Kruskal.traverse(roadNetwork);
        double kruskalTotal = 0.0;

        System.out.println("Réseau routier minimal (Kruskal) :");
        if (kruskalMst.isEmpty()) {
            System.out.println("Aucune arête dans l'ACPM (Kruskal) — vérifie le graphe ou l'implémentation.");
        } else {
            for (Edge e : kruskalMst) {
                System.out.println(" - " + e.getSource().getId() + " -- " + e.getTarget().getId() +
                        " (Coût: " + e.getWeight() + ")");
                kruskalTotal += e.getWeight();
            }
            System.out.println("Coût total du réseau minimal (Kruskal): " + kruskalTotal);
        }

        // --- Part III: Recherche du chemin optimal (Dijkstra) ---
        System.out.println("\n\n--- Part III: Plus court chemin (Dijkstra) ---");
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

        // --- Part IV: Recherche du chemin optimal (Floyd-Warshall) ---
        System.out.println("\n\n--- Part IV: Matrice des plus courts chemins (Floyd-Warshall) ---");

        Map<String, Map<String, Double>> shortestPaths = FloydWarshall.floydWarshall(roadNetwork);

        // Get sorted city names for printing the matrix
        List<String> cities = new ArrayList<>(shortestPaths.keySet());
        Collections.sort(cities);

        // Print header row
        System.out.printf("%-10s", ""); // Empty corner
        for (String city : cities) {
            System.out.printf("%-10s", city.substring(0, Math.min(city.length(), 10)));
        }
        System.out.println();

        // Print each city's row
        for (String rowCity : cities) {
            System.out.printf("%-10s", rowCity.substring(0, Math.min(rowCity.length(), 10)));
            for (String colCity : cities) {
                double dist = shortestPaths.get(rowCity).get(colCity);
                if (dist == Double.POSITIVE_INFINITY) {
                    System.out.printf("%-10s", "Inf");
                } else {
                    System.out.printf("%-10.0f", dist); // Print distance as a whole number
                }
            }
            System.out.println();
        }

        // --- Part V: Plus courts chemins depuis une source (Bellman-Ford) ---
        System.out.println("\n\n--- Part V: Plus courts chemins depuis une source (Bellman-Ford) ---");
        String bfSource = "Rennes";
        try {
            Map<Node, Double> distances = BellmanFord.shortestPaths(roadNetwork, bfSource);

            System.out.println("Distances depuis " + bfSource + " :");
            for (Map.Entry<Node, Double> entry : distances.entrySet()) {
                System.out.println(" - " + entry.getKey().getId() + " = " + entry.getValue());
            }
        } catch (RuntimeException ex) {
            System.out.println("Erreur (Bellman-Ford) : " + ex.getMessage());
        }
    }
}
