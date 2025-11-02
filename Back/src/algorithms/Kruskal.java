package algorithms;

import models.Edge;
import models.Graph;
import models.Node;

import java.util.*;

public class Kruskal {

    /**
     * Implémente l'algorithme de Kruskal pour trouver l'Arbre Couvrant
     * de Poids Minimum (ACPM) d'un graphe.
     *
     * L'algorithme trie toutes les arêtes par poids croissant, puis les ajoute
     * à l'arbre si elles ne forment pas un cycle (vérifié avec Union-Find).
     *
     * @param graph   Le graphe (non-dirigé) à traiter.
     * @return Une liste d'arêtes (List<Edge>) représentant l'ACPM.
     */
    public static List<Edge> traverse(Graph graph) {
        List<Edge> ACPM = new ArrayList<>();

        //Etape 1 : Trier les arrêtes par ordre croissant
        List<Edge> edges = new ArrayList<>(graph.getAllEdges());
        edges.sort(Comparator.comparingDouble(Edge::getWeight));

        //Etape 2 : Initialisé Union Find
        UnionFind uf = new UnionFind(graph.getNodes());

        //Etapes 3 : Montrer les arrêtes triées
        for (Edge edge : edges) {
            Node u = edge.getSource();
            Node v = edge.getTarget();

            //si les sommets ne se connecte pas, on relie
            if (uf.find(u) != uf.find(v)) {
                uf.union(u, v);
                ACPM.add(edge);
            }
        }

        return ACPM;
    }

    /**
     * classe interne Union-Find
     */
    private static class UnionFind {
        private final Map<Node, Node> parent;

        public UnionFind(Collection<Node> nodes) {
            parent = new HashMap<>();
            for (Node node : nodes) {
                parent.put(node, node);
            }
        }

        public Node find(Node n) {
            Node p = parent.get(n);
            if (p != n) {
                parent.put(n, find(p)); // compression de chemin
            }
            return parent.get(n);
        }

        public void union(Node a, Node b) {
            Node rootA = find(a);
            Node rootB = find(b);
            if (rootA != rootB) {
                parent.put(rootB, rootA);
            }
        }
    }
}
