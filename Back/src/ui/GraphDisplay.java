package ui;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Edge; // Import depuis Back
import models.Graph; // Import depuis Back
import models.Node; // Import depuis Back

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un Pane JavaFX qui dessine un Graphe avec des nœuds cliquables et déplaçables.
 */
public class GraphDisplay extends Pane {

    private final Graph graph;
    private final Map<String, Point2D> nodeCoordinates;


    // Stocke les objets Circle pour chaque ID de nœud
    private final Map<String, Circle> nodeCircles = new HashMap<>();
    // Stocke les objets Text (labels) pour chaque ID de nœud
    private final Map<String, Text> nodeLabels = new HashMap<>();
    // Stocke la liste des lignes connectées à chaque nœud
    private final Map<String, List<Line>> nodeConnectedLines = new HashMap<>();
    // Stocke le label de poids pour chaque objet Line
    private final Map<Line, Text> lineWeightLabels = new HashMap<>();

    /**
     * Classe interne pour stocker le contexte lors d'un glisser-déposer.
     */
    private static class DragContext {
        double mouseAnchorX;
        double mouseAnchorY;
    }

    public GraphDisplay(Graph graph) {
        this.graph = graph;
        this.nodeCoordinates = new HashMap<>();

        setMinWidth(700);
        setMinHeight(600);
        setStyle("-fx-background-color: #FAFAFA;");

        defineCoordinates();
        drawGraph();
    }

    /**
     * Dessine l'intégralité du graphe (nœuds et arêtes).
     */
    private void drawGraph() {
        if (graph == null) return;

        // Vider les anciennes références
        getChildren().clear();
        nodeCircles.clear();
        nodeLabels.clear();
        nodeConnectedLines.clear();
        lineWeightLabels.clear();

        // Initialiser la map des lignes connectées
        for (Node node : graph.getNodes()) {
            nodeConnectedLines.put(node.getId(), new ArrayList<>());
        }

        // 1. Dessiner les arêtes et les poids
        for (Node sourceNode : graph.getNodes()) {
            Point2D sourcePoint = nodeCoordinates.get(sourceNode.getId());
            if (sourcePoint == null) continue;

            for (Edge edge : sourceNode.getEdges()) {
                Node targetNode = edge.getTarget();
                Point2D targetPoint = nodeCoordinates.get(targetNode.getId());
                if (targetPoint == null) continue;

                // Éviter de dessiner A->B et B->A
                if (sourceNode.getId().compareTo(targetNode.getId()) > 0) continue;

                Line line = new Line(sourcePoint.getX(), sourcePoint.getY(), targetPoint.getX(), targetPoint.getY());
                line.setStroke(Color.GRAY);
                line.setStrokeWidth(2);

                Text weightText = new Text(String.valueOf((int) edge.getWeight()));
                weightText.setX((sourcePoint.getX() + targetPoint.getX()) / 2 + 5);
                weightText.setY((sourcePoint.getY() + targetPoint.getY()) / 2 + 5);
                weightText.setFill(Color.DARKSLATEBLUE);

                getChildren().addAll(line, weightText);

                // ----- Stocker les références -----
                nodeConnectedLines.get(sourceNode.getId()).add(line);
                nodeConnectedLines.get(targetNode.getId()).add(line);
                lineWeightLabels.put(line, weightText);
                // ----------------------------------
            }
        }

        // 2. Dessiner les nœuds (villes) et leurs labels
        for (Node node : graph.getNodes()) {
            Point2D point = nodeCoordinates.get(node.getId());
            if (point == null) continue;

            Circle circle = new Circle(point.getX(), point.getY(), 10);
            circle.setFill(Color.LIGHTBLUE);
            circle.setStroke(Color.BLACK);

            Text text = new Text(point.getX() - 15, point.getY() - 15, node.getId());
            text.setStyle("-fx-font-weight: bold;");

            getChildren().addAll(circle, text);

            // ----- Stocker les références -----
            nodeCircles.put(node.getId(), circle);
            nodeLabels.put(node.getId(), text);
            // ----------------------------------

            // ----- AJOUTER LES GESTIONNAIRES D'ÉVÉNEMENTS -----
            addDragHandlers(circle, text, node.getId());
            // -------------------------------------------------
        }
    }

    /**
     * Ajoute les gestionnaires d'événements pour le clic et le glisser-déposer.
     */
    private void addDragHandlers(Circle circle, Text text, String nodeId) {
        final DragContext dragContext = new DragContext();
        final boolean[] isDragging = {false};

        circle.setOnMouseEntered(e -> circle.setCursor(Cursor.HAND));
        circle.setOnMouseExited(e -> circle.setCursor(Cursor.DEFAULT));

        // --- GESTION DU "CLIC" ---
        circle.setOnMouseClicked(e -> {
            if (!isDragging[0]) {
                System.out.println("Clic sur: " + nodeId);
                nodeCircles.values().forEach(c -> c.setFill(Color.LIGHTBLUE));
                circle.setFill(Color.ORANGE);
            }
            e.consume(); // Empêche l'événement de se propager
        });

        // --- GESTION DU "DRAG & DROP" ---

        // 1. Quand on appuie sur la souris
        circle.setOnMousePressed(e -> {
            isDragging[0] = false;
            // Stocker la position de la souris
            dragContext.mouseAnchorX = e.getSceneX();
            dragContext.mouseAnchorY = e.getSceneY();

            circle.toFront();
            text.toFront();
            e.consume();
        });

        // 2. Quand on déplace la souris (en tenant le clic)
        circle.setOnMouseDragged(e -> {
            isDragging[0] = true;

            // Calculer le "delta" (le déplacement depuis le dernier événement)
            double deltaX = e.getSceneX() - dragContext.mouseAnchorX;
            double deltaY = e.getSceneY() - dragContext.mouseAnchorY;

            // Nouvelle position du nœud (ancienne position + delta)
            double newX = circle.getCenterX() + deltaX;
            double newY = circle.getCenterY() + deltaY;

            // Mettre à jour le CERCLE et le LABEL
            circle.setCenterX(newX);
            circle.setCenterY(newY);
            text.setX(newX - 15);
            text.setY(newY - 15);

            // Mettre à jour les LIGNES connectées
            double epsilon = 1e-6; // Tolérance pour la comparaison des doubles

            for (Line line : nodeConnectedLines.get(nodeId)) {
                Text weight = lineWeightLabels.get(line);

                // Si le nœud est le point de DÉPART de la ligne
                // (On compare l'ancienne position (newX - deltaX) pour savoir quel bout bouger)
                if (Math.abs(line.getStartX() - (newX - deltaX)) < epsilon &&
                        Math.abs(line.getStartY() - (newY - deltaY)) < epsilon) {

                    line.setStartX(newX);
                    line.setStartY(newY);
                }
                // Si le nœud est le point d'ARRIVÉE de la ligne
                else if (Math.abs(line.getEndX() - (newX - deltaX)) < epsilon &&
                        Math.abs(line.getEndY() - (newY - deltaY)) < epsilon) {

                    line.setEndX(newX);
                    line.setEndY(newY);
                }

                // Mettre à jour la position du POIDS
                weight.setX((line.getStartX() + line.getEndX()) / 2 + 5);
                weight.setY((line.getStartY() + line.getEndY()) / 2 + 5);
            }

            // Mettre à jour l'ancre de la souris pour le PROCHAIN événement
            dragContext.mouseAnchorX = e.getSceneX();
            dragContext.mouseAnchorY = e.getSceneY();
            e.consume();
        });

        // 3. Quand on relâche la souris
        circle.setOnMouseReleased(e -> {
            // Le flag 'isDragging' sera réinitialisé au prochain clic
        });
    }


    /**
     * Définit les positions X/Y des villes sur le canvas.
     */
    private void defineCoordinates() {
        // (Coordonnées inchangées)
        nodeCoordinates.put("Lille", new Point2D(550, 100));
        nodeCoordinates.put("Caen", new Point2D(300, 120));
        nodeCoordinates.put("Paris", new Point2D(400, 250));
        nodeCoordinates.put("Nancy", new Point2D(650, 300));
        nodeCoordinates.put("Rennes", new Point2D(150, 200));
        nodeCoordinates.put("Dijon", new Point2D(520, 400));
        nodeCoordinates.put("Nantes", new Point2D(250, 300));
        nodeCoordinates.put("Lyon", new Point2D(550, 550));
        nodeCoordinates.put("Grenoble", new Point2D(620, 600));
        nodeCoordinates.put("Bordeaux", new Point2D(250, 500));
    }

    /**
     * Réinitialise les couleurs de tous les nœuds et arêtes.
     */
    public void resetDisplay() {
        // Réinitialiser les lignes
        for (Line line : lineWeightLabels.keySet()) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
        }
        // Réinitialiser les nœuds
        for (Circle circle : nodeCircles.values()) {
            circle.setFill(Color.LIGHTBLUE);
        }
    }

    /**
     * Met en surbrillance un chemin (liste de nœuds).
     * @param path La liste des nœuds dans l'ordre du chemin.
     */
    public void highlightPath(List<Node> path) {
        if (path == null || path.isEmpty()) return;

        // Mettre les nœuds du chemin en orange
        for (Node node : path) {
            Circle circle = nodeCircles.get(node.getId());
            if (circle != null) {
                circle.setFill(Color.ORANGE);
            }
        }

        // Mettre les arêtes du chemin en rouge
        for (int i = 0; i < path.size() - 1; i++) {
            Node nodeA = path.get(i);
            Node nodeB = path.get(i + 1);

            // Trouver la ligne qui connecte A et B
            for (Line line : nodeConnectedLines.get(nodeA.getId())) {
                Text weight = lineWeightLabels.get(line);
                if (weight == null) continue; // Ligne non valide

                // Obtenir les ID des nœuds de la ligne
                Circle circleA = nodeCircles.get(nodeA.getId());
                Circle circleB = nodeCircles.get(nodeB.getId());

                // Vérifier si cette ligne connecte bien A et B
                if ((line.getStartX() == circleA.getCenterX() && line.getStartY() == circleA.getCenterY() &&
                        line.getEndX() == circleB.getCenterX() && line.getEndY() == circleB.getCenterY()) ||
                        (line.getStartX() == circleB.getCenterX() && line.getStartY() == circleB.getCenterY() &&
                                line.getEndX() == circleA.getCenterX() && line.getEndY() == circleA.getCenterY()))
                {
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(4);
                    line.toFront();
                    weight.toFront();
                    break;
                }
            }
        }

        // Ramener les nœuds au premier plan
        nodeCircles.values().forEach(Circle::toFront);
        nodeLabels.values().forEach(Text::toFront);
    }

    /**
     * Met en surbrillance un seul nœud.
     * @param node Le nœud à surligner.
     * @param color La couleur de surbrillance.
     */
    public void highlightNode(Node node, Color color) {
        Circle circle = nodeCircles.get(node.getId());
        if (circle != null) {
            circle.setFill(color);
            circle.toFront();

            Text label = nodeLabels.get(node.getId());
            if (label != null) {
                label.toFront();
            }
        }
    }

    /**
     * Met en surbrillance une liste de nœuds en séquence, avec un délai.
     * Parfait pour visualiser l'ordre de visite de BFS ou DFS.
     * @param nodes La liste des nœuds dans l'ordre de visite.
     */
    public void highlightNodesSequentially(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) return;

        resetDisplay();

        Timeline timeline = new Timeline();
        // Délai entre chaque nœud (ex: 300 ms)
        double delay = 300.0;

        for (int i = 0; i < nodes.size(); i++) {
            final Node node = nodes.get(i);

            // Le premier nœud (départ) sera orange, les autres en jaune
            Color color = (i == 0) ? Color.ORANGE : Color.GOLD;

            // Créer une "KeyFrame" (une étape dans l'animation)
            KeyFrame kf = new KeyFrame(Duration.millis(i * delay), e -> {
                highlightNode(node, color);
            });

            timeline.getKeyFrames().add(kf);
        }

        // Lancer l'animation
        timeline.play();
    }

    /**
     * Met en surbrillance les arêtes d'un Arbre Couvrant (MST).
     * @param mstEdges La liste des arêtes de l'arbre.
     */
    public void highlightMST(List<Edge> mstEdges) {
        if (mstEdges == null || mstEdges.isEmpty()) return;

        // Réinitialiser le graphe
        resetDisplay();

        // Mettre les arêtes de l'arbre en vert
        for (Edge edge : mstEdges) {
            Node nodeA = edge.getSource();
            Node nodeB = edge.getTarget();

            // Trouver la ligne qui connecte A et B
            for (Line line : nodeConnectedLines.get(nodeA.getId())) {
                Text weight = lineWeightLabels.get(line);
                if (weight == null) continue;

                Circle circleA = nodeCircles.get(nodeA.getId());
                Circle circleB = nodeCircles.get(nodeB.getId());

                // Vérifier si cette ligne connecte bien A et B (dans n'importe quel sens)
                if ((Math.abs(line.getStartX() - circleA.getCenterX()) < 1e-6 &&
                        Math.abs(line.getStartY() - circleA.getCenterY()) < 1e-6 &&
                        Math.abs(line.getEndX() - circleB.getCenterX()) < 1e-6 &&
                        Math.abs(line.getEndY() - circleB.getCenterY()) < 1e-6) ||
                        (Math.abs(line.getStartX() - circleB.getCenterX()) < 1e-6 &&
                                Math.abs(line.getStartY() - circleB.getCenterY()) < 1e-6 &&
                                Math.abs(line.getEndX() - circleA.getCenterX()) < 1e-6 &&
                                Math.abs(line.getEndY() - circleA.getCenterY()) < 1e-6))
                {
                    line.setStroke(Color.GREEN); // Couleur pour le MST
                    line.setStrokeWidth(4);
                    line.toFront();
                    weight.toFront();

                    // Surligner aussi les nœuds connectés
                    circleA.setFill(Color.LIGHTGREEN);
                    circleB.setFill(Color.LIGHTGREEN);

                    break;
                }
            }
        }

        // Ramener tous les nœuds et labels au premier plan
        nodeCircles.values().forEach(Circle::toFront);
        nodeLabels.values().forEach(Text::toFront);
    }

    /**
     * Met en surbrillance les arêtes d'un Arbre (MST, BFS, etc.).
     * @param treeEdges La liste des arêtes de l'arbre.
     * @param color La couleur à utiliser pour la surbrillance.
     */
    public void highlightTree(List<Edge> treeEdges, Color color) {
        if (treeEdges == null || treeEdges.isEmpty()) return;

        resetDisplay();

        Color nodeColor = color.deriveColor(1, 1, 1, 0.5); // Couleur plus claire pour les nœuds

        for (Edge edge : treeEdges) {
            Node nodeA = edge.getSource();
            Node nodeB = edge.getTarget();

            // Trouver la ligne qui connecte A et B
            for (Line line : nodeConnectedLines.get(nodeA.getId())) {
                Text weight = lineWeightLabels.get(line);
                if (weight == null) continue;

                Circle circleA = nodeCircles.get(nodeA.getId());
                Circle circleB = nodeCircles.get(nodeB.getId());

                // Vérifier si cette ligne connecte bien A et B (dans n'importe quel sens)
                if ((Math.abs(line.getStartX() - circleA.getCenterX()) < 1e-6 &&
                        Math.abs(line.getStartY() - circleA.getCenterY()) < 1e-6 &&
                        Math.abs(line.getEndX() - circleB.getCenterX()) < 1e-6 &&
                        Math.abs(line.getEndY() - circleB.getCenterY()) < 1e-6) ||
                        (Math.abs(line.getStartX() - circleB.getCenterX()) < 1e-6 &&
                                Math.abs(line.getStartY() - circleB.getCenterY()) < 1e-6 &&
                                Math.abs(line.getEndX() - circleA.getCenterX()) < 1e-6 &&
                                Math.abs(line.getEndY() - circleA.getCenterY()) < 1e-6))
                {
                    line.setStroke(color);
                    line.setStrokeWidth(4);
                    line.toFront();
                    weight.toFront();

                    // Surligner aussi les nœuds connectés
                    circleA.setFill(nodeColor);
                    circleB.setFill(nodeColor);

                    break;
                }
            }
        }

        nodeCircles.values().forEach(Circle::toFront);
        nodeLabels.values().forEach(Text::toFront);
    }
}