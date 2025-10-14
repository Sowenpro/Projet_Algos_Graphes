package models;

public class Edge {
    private final Node source;
    private final Node target;
    private final double weight; // utile pour les algos pondérés

    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
