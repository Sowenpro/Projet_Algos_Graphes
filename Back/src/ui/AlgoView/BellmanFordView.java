package ui.AlgoView;

import algorithms.BellmanFord;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Graph;
import models.GraphBuilder;
import models.Node;
import ui.AlgoMenu;
import ui.GraphDisplay;

import java.util.*; // Import complet
import java.util.stream.Collectors;

public class BellmanFordView {

    private final BorderPane view;
    private final GraphDisplay graphDisplay;
    private final Graph roadNetwork;
    private final Label infoLabel;

    public BellmanFordView(Stage stage) {
        this.roadNetwork = GraphBuilder.createRoadNetwork();
        this.graphDisplay = new GraphDisplay(roadNetwork);

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        List<String> cityNames = roadNetwork.getNodes().stream()
                .map(Node::getId)
                .sorted()
                .collect(Collectors.toList());

        // Contrôles
        Label startLabel = new Label("Départ:");
        ComboBox<String> startCityBox = new ComboBox<>(FXCollections.observableList(cityNames));
        Label endLabel = new Label("Arrivée:");
        ComboBox<String> endCityBox = new ComboBox<>(FXCollections.observableList(cityNames));
        Button runButton = new Button("Trouver le chemin");
        Button resetButton = new Button("Réinitialiser");
        Button backButton = new Button("⬅ Retour");
        backButton.setStyle("-fx-text-fill: red;");

        infoLabel = new Label("Info: Bellman-Ford gère les poids négatifs.");
        infoLabel.setStyle("-fx-padding: 0 0 0 15;");

        controls.getChildren().addAll(backButton, startLabel, startCityBox, endLabel, endCityBox, runButton, resetButton, infoLabel);

        // --- Actions ---

        backButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            stage.setScene(new Scene(algoMenu.getView(), 600, 400));
        });

        resetButton.setOnAction(e -> {
            graphDisplay.resetDisplay();
            infoLabel.setText("Info: Bellman-Ford gère les poids négatifs.");
            infoLabel.setTextFill(Color.BLACK);
        });

        // Action du bouton "Trouver le chemin"
        runButton.setOnAction(e -> {
            String startId = startCityBox.getValue();
            String endId = endCityBox.getValue();

            if (startId != null && endId != null) {
                graphDisplay.resetDisplay();

                // 2. Appeler algorithme
                Map<String, Object> result = BellmanFord.findShortestPaths(roadNetwork, startId);

                // 3. Vérifier les résultats
                boolean hasNegativeCycle = (boolean) result.get("negativeCycle");

                if (hasNegativeCycle) {
                    infoLabel.setText("ERREUR : Cycle de poids négatif détecté !");
                    infoLabel.setTextFill(Color.RED);
                } else {
                    // Récupérer les maps de résultats
                    Map<Node, Double> distances = (Map<Node, Double>) result.get("distances");
                    Map<Node, Node> predecessors = (Map<Node, Node>) result.get("predecessors");

                    Node endNode = roadNetwork.getNode(endId);
                    double distance = distances.get(endNode);

                    // 4. Reconstruire le chemin
                    List<Node> path = reconstructPath(predecessors, endNode);

                    if (distance == Double.POSITIVE_INFINITY) {
                        infoLabel.setText("Aucun chemin trouvé.");
                        infoLabel.setTextFill(Color.ORANGE);
                    } else {
                        // 5. Mettre à jour l'affichage
                        graphDisplay.highlightPath(path);
                        infoLabel.setText(String.format("Distance: %.0f", distance));
                        infoLabel.setTextFill(Color.BLACK);
                    }
                }
            }
        });

        // 4. Mettre en page la vue
        view = new BorderPane();
        view.setTop(controls);
        view.setCenter(graphDisplay);
    }

    /**
     * Méthode utilitaire pour reconstruire le chemin à partir de la map des prédécesseurs.
     */
    private List<Node> reconstructPath(Map<Node, Node> predecessors, Node endNode) {
        List<Node> path = new ArrayList<>();
        Node step = endNode;

        // Tant que le nœud existe et qu'il a un prédécesseur (ou est le nœud de départ)
        while (step != null) {
            path.add(step);
            step = predecessors.get(step);
        }

        Collections.reverse(path); // Mettre le chemin dans le bon ordre

        // Si le premier nœud n'est pas le nœud de départ (pas de chemin), retourner vide
        if (!path.isEmpty() && predecessors.containsKey(path.get(0))) {
            return new ArrayList<>(); // Pas de chemin
        }

        return path;
    }

    public BorderPane getView() {
        return view;
    }
}