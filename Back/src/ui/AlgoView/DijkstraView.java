package ui.AlgoView;

import algorithms.Dijkstra; // Importe depuis le module Back
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Graph; // Importe depuis Back
import models.GraphBuilder; // Importe depuis Back
import models.Node; // Importe depuis Back
import ui.AlgoMenu;
import ui.GraphDisplay;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DijkstraView {

    private final BorderPane view;
    private final GraphDisplay graphDisplay;
    private final Graph roadNetwork;

    public DijkstraView(Stage stage) {
        // 1. Charger le graphe et créer l'affichage
        this.roadNetwork = GraphBuilder.createRoadNetwork();
        this.graphDisplay = new GraphDisplay(roadNetwork);

        // 2. Créer la barre de contrôle du haut
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        // Liste des villes pour les menus déroulants
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

        controls.getChildren().addAll(backButton, startLabel, startCityBox, endLabel, endCityBox, runButton, resetButton);

        // 3. Définir les actions

        // Action du bouton "Retour"
        backButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            stage.setScene(new Scene(algoMenu.getView(), 600, 400));
        });

        // Action du bouton "Réinitialiser"
        resetButton.setOnAction(e -> {
            graphDisplay.resetDisplay();
        });

        // Action du bouton "Trouver le chemin" (Dijkstra)
        runButton.setOnAction(e -> {
            String start = startCityBox.getValue();
            String end = endCityBox.getValue();

            if (start != null && end != null) {
                // 1. Réinitialiser l'affichage
                graphDisplay.resetDisplay();

                // 2. Appeler l'algorithme du module Back
                Map<String, Object> result = Dijkstra.dijkstra(roadNetwork, start, end);

                // 3. Récupérer le chemin
                List<Node> path = (List<Node>) result.get("path");

                // 4. Mettre à jour l'affichage avec le résultat
                graphDisplay.highlightPath(path);
            }
        });

        // 4. Mettre en page la vue
        view = new BorderPane();
        view.setTop(controls);
        view.setCenter(graphDisplay);
    }

    public BorderPane getView() {
        return view;
    }
}