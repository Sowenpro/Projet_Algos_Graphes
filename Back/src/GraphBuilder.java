import models.*;


public class GraphBuilder {

    /**
     * Creates the road network graph based on the project PDF.
     * This uses the original (directed) graph implementation.
     */
    public static Graph createRoadNetwork() {
        Graph graph = new Graph();

        // 1. Add all nodes (cities)
        // We'll pass 0,0 for x,y as they aren't used in your Node class
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

        // 2. Add all edges (routes) with weights in both directions

        graph.addEdge("Rennes", "Caen", 75);
        graph.addEdge("Caen", "Rennes", 75); // Reverse

        graph.addEdge("Rennes", "Paris", 110);
        graph.addEdge("Paris", "Rennes", 110); // Reverse

        graph.addEdge("Rennes", "Nantes", 45);
        graph.addEdge("Nantes", "Rennes", 45); // Reverse

        graph.addEdge("Rennes", "Bordeaux", 130);
        graph.addEdge("Bordeaux", "Rennes", 130); // Reverse

        graph.addEdge("Caen", "Paris", 50);
        graph.addEdge("Paris", "Caen", 50); // Reverse

        graph.addEdge("Caen", "Lille", 65);
        graph.addEdge("Lille", "Caen", 65); // Reverse

        graph.addEdge("Lille", "Paris", 70);
        graph.addEdge("Paris", "Lille", 70); // Reverse

        graph.addEdge("Lille", "Nancy", 100);
        graph.addEdge("Nancy", "Lille", 100); // Reverse

        graph.addEdge("Lille", "Dijon", 120);
        graph.addEdge("Dijon", "Lille", 120); // Reverse

        graph.addEdge("Paris", "Nantes", 80);
        graph.addEdge("Nantes", "Paris", 80); // Reverse

        graph.addEdge("Paris", "Bordeaux", 150);
        graph.addEdge("Bordeaux", "Paris", 150); // Reverse

        graph.addEdge("Paris", "Dijon", 60);
        graph.addEdge("Dijon", "Paris", 60); // Reverse

        graph.addEdge("Nantes", "Bordeaux", 90);
        graph.addEdge("Bordeaux", "Nantes", 90); // Reverse

        graph.addEdge("Bordeaux", "Lyon", 100);
        graph.addEdge("Lyon", "Bordeaux", 100); // Reverse

        graph.addEdge("Dijon", "Nancy", 75);
        graph.addEdge("Nancy", "Dijon", 75); // Reverse

        graph.addEdge("Dijon", "Lyon", 70);
        graph.addEdge("Lyon", "Dijon", 70); // Reverse

        graph.addEdge("Dijon", "Grenoble", 75);
        graph.addEdge("Grenoble", "Dijon", 75); // Reverse

        graph.addEdge("Nancy", "Lyon", 90);
        graph.addEdge("Lyon", "Nancy", 90); // Reverse

        graph.addEdge("Nancy", "Grenoble", 80);
        graph.addEdge("Grenoble", "Nancy", 80); // Reverse

        graph.addEdge("Lyon", "Grenoble", 40);
        graph.addEdge("Grenoble", "Lyon", 40); // Reverse

        return graph;
    }
}
