package models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String id;
    private final List<Edge> edges;

    public Node(String id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public String toString() {
        return id;
    }
}
