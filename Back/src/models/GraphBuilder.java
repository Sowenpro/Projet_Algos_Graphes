package models;

public class GraphBuilder {

    /*
     * Crée le graphe du réseau routier en se basant sur le PDF du projet.
     *
     * Cette version ajoute les arêtes dans les deux sens (A->B et B->A)
     * pour simuler un graphe non-dirigé, ce qui est nécessaire pour
     * la plupart des algorithmes (Prim, Kruskal, Dijkstra).
     */
    public static Graph createRoadNetwork() {
        Graph graph = new Graph();

        // 1. Ajouter tous les nœuds (villes)
        // On passe 0,0 pour x,y car ils ne sont pas utilisés dans la classe Node
        graph.addNode("Rennes", 0, 0);
        graph.addNode("Caen", 0, 0);
        graph.addNode("Lille", 0, 0);
        graph.addNode("Paris", 0, 0);
        graph.addNode("Nantes", 0, 0);
        graph.addNode("Bordeaux", 0, 0);
        graph.addNode("Dijon", 0, 0);
        graph.addNode("Nancy", 0, 0);
        graph.addNode("Lyon", 0, 0);
        graph.addNode("Grenoble", 0, 0);

        // 2. Ajouter toutes les arêtes (routes) avec les poids dans les deux sens

        graph.addEdge("Rennes", "Caen", 75);
        graph.addEdge("Caen", "Rennes", 75); // Inverse

        graph.addEdge("Rennes", "Paris", 110);
        graph.addEdge("Paris", "Rennes", 110); // Inverse

        graph.addEdge("Rennes", "Nantes", 45);
        graph.addEdge("Nantes", "Rennes", 45); // Inverse

        graph.addEdge("Rennes", "Bordeaux", 130);
        graph.addEdge("Bordeaux", "Rennes", 130); // Inverse

        graph.addEdge("Caen", "Paris", 50);
        graph.addEdge("Paris", "Caen", 50); // Inverse

        graph.addEdge("Caen", "Lille", 65);
        graph.addEdge("Lille", "Caen", 65); // Inverse

        graph.addEdge("Lille", "Paris", 70);
        graph.addEdge("Paris", "Lille", 70); // Inverse

        graph.addEdge("Lille", "Nancy", 100);
        graph.addEdge("Nancy", "Lille", 100); // Inverse

        graph.addEdge("Lille", "Dijon", 120);
        graph.addEdge("Dijon", "Lille", 120); // Inverse

        graph.addEdge("Paris", "Nantes", 80);
        graph.addEdge("Nantes", "Paris", 80); // Inverse

        graph.addEdge("Paris", "Bordeaux", 150);
        graph.addEdge("Bordeaux", "Paris", 150); // Inverse

        graph.addEdge("Paris", "Dijon", 60);
        graph.addEdge("Dijon", "Paris", 60); // Inverse

        graph.addEdge("Nantes", "Bordeaux", 90);
        graph.addEdge("Bordeaux", "Nantes", 90); // Inverse

        graph.addEdge("Bordeaux", "Lyon", 100);
        graph.addEdge("Lyon", "Bordeaux", 100); // Inverse

        graph.addEdge("Dijon", "Nancy", 75);
        graph.addEdge("Nancy", "Dijon", 75); // Inverse

        graph.addEdge("Dijon", "Lyon", 70);
        graph.addEdge("Lyon", "Dijon", 70); // Inverse

        graph.addEdge("Dijon", "Grenoble", 75);
        graph.addEdge("Grenoble", "Dijon", 75); // Inverse

        graph.addEdge("Nancy", "Lyon", 90);
        graph.addEdge("Lyon", "Nancy", 90); // Inverse

        graph.addEdge("Nancy", "Grenoble", 80);
        graph.addEdge("Grenoble", "Nancy", 80); // Inverse

        graph.addEdge("Lyon", "Grenoble", 40);
        graph.addEdge("Grenoble", "Lyon", 40); // Inverse

        return graph;
    }
}