package ui.AlgoView;

import algorithms.BFS; // Importe depuis le module Back
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
import models.Edge;
import models.Graph; // Importe depuis Back
import models.GraphBuilder; // Importe depuis Back
import models.Node; // Importe depuis Back
import ui.AlgoMenu;
import ui.GraphDisplay;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BFSView {

    private final BorderPane view;
    private final GraphDisplay graphDisplay;
    private final Graph roadNetwork;

    public BFSView(Stage stage) {
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
        Label startLabel = new Label("Ville de départ:");
        ComboBox<String> startCityBox = new ComboBox<>(FXCollections.observableList(cityNames));

        Button runAnimButton = new Button("Lancer Parcours (Anim)");
        Button showTreeButton = new Button("Afficher l'Arbre BFS"); // <-- NOUVEAU BOUTON
        Button resetButton = new Button("Réinitialiser");
        Button backButton = new Button("⬅ Retour");
        backButton.setStyle("-fx-text-fill: red;");

        controls.getChildren().addAll(backButton, startLabel, startCityBox, runAnimButton, showTreeButton, resetButton);

        // Actions
        backButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            stage.setScene(new Scene(algoMenu.getView(), 600, 400));
        });

        resetButton.setOnAction(e -> {
            graphDisplay.resetDisplay();
        });

        // Action pour l'animation de parcours
        runAnimButton.setOnAction(e -> {
            String start = startCityBox.getValue();
            if (start != null) {
                graphDisplay.resetDisplay();
                // Appeler l'algorithme
                Map<String, Object> result = BFS.bfs(roadNetwork, start);
                // Récupérer l'ordre de visite
                List<Node> visitOrder = (List<Node>) result.get("visitOrder");
                // Lancer l'animation
                graphDisplay.highlightNodesSequentially(visitOrder);
            }
        });

        // ACTION pour afficher l'arbre
        showTreeButton.setOnAction(e -> {
            String start = startCityBox.getValue();
            if (start != null) {
                graphDisplay.resetDisplay();
                // Appeler l'algorithme
                Map<String, Object> result = BFS.bfs(roadNetwork, start);
                // Récupérer les arêtes de l'arbre
                List<Edge> treeEdges = (List<Edge>) result.get("treeEdges");
                // Afficher l'arbre (par ex: en bleu)
                graphDisplay.highlightTree(treeEdges, Color.BLUEVIOLET);
            }
        });

        view = new BorderPane();
        view.setTop(controls);
        view.setCenter(graphDisplay);
    }

    public BorderPane getView() {
        return view;
    }
}