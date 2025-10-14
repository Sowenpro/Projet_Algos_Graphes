package models;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(String id, double x, double y) {
        nodes.putIfAbsent(id, new Node(id));
    }

    public void addEdge(String sourceId, String targetId, double weight) {
        Node source = nodes.get(sourceId);
        Node target = nodes.get(targetId);
        if (source == null || target == null) return;

        Edge edge = new Edge(source, target, weight);
        source.addEdge(edge);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Node n : nodes.values()) {
            edges.addAll(n.getEdges());
        }
        return edges;
    }

    public void clear() {
        nodes.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodes.values()) {
            sb.append(node).append(" : ").append(node.getEdges()).append("\n");
        }
        return sb.toString();
    }
}
