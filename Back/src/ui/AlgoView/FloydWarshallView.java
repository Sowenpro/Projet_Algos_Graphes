package ui.AlgoView;

import algorithms.FloydWarshall;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Graph;
import models.GraphBuilder;
import models.Node;
import ui.AlgoMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FloydWarshallView {

    private final BorderPane view;
    private final Graph roadNetwork;
    private final GridPane matrixGrid; // Pour afficher la matrice
    private final ScrollPane scrollPane; // Pour faire défiler la matrice

    public FloydWarshallView(Stage stage) {
        this.roadNetwork = GraphBuilder.createRoadNetwork();

        // 1. Créer la barre de contrôle du haut
        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        // Contrôles
        Button runButton = new Button("Calculer la Matrice");
        Button backButton = new Button("⬅ Retour");
        backButton.setStyle("-fx-text-fill: red;");

        controls.getChildren().addAll(backButton, runButton);

        // 2. Créer la grille pour la matrice
        matrixGrid = new GridPane();
        matrixGrid.setPadding(new Insets(10));
        matrixGrid.setHgap(10);
        matrixGrid.setVgap(10);
        matrixGrid.setStyle("-fx-background-color: #f4f4f4;");

        // 3. Mettre la grille dans un ScrollPane
        scrollPane = new ScrollPane(matrixGrid);
        scrollPane.setFitToWidth(true);

        // 4. Définir les actions

        backButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            stage.setScene(new Scene(algoMenu.getView(), 600, 400));
        });

        // Action du bouton "Calculer la Matrice"
        runButton.setOnAction(e -> {
            // Appeler VOTRE algorithme
            Map<String, Map<String, Double>> matrix = FloydWarshall.floydWarshall(roadNetwork);
            // Afficher la matrice dans la grille
            displayMatrix(matrix);
        });

        // 5. Mettre en page la vue
        view = new BorderPane();
        view.setTop(controls);
        view.setCenter(scrollPane);

        // Afficher une grille vide au début
        displayMatrix(null);
    }

    /**
     * Remplit le GridPane avec les résultats de l'algorithme.
     */
    private void displayMatrix(Map<String, Map<String, Double>> matrix) {
        matrixGrid.getChildren().clear(); // Nettoyer l'ancienne matrice

        // Obtenir la liste des villes
        List<String> cities = new ArrayList<>();
        for (Node n : roadNetwork.getNodes()) {
            cities.add(n.getId());
        }
        Collections.sort(cities);

        // 1. Créer les en-têtes (Colonnes)
        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            Label header = new Label(city);
            header.setStyle("-fx-font-weight: bold;");
            matrixGrid.add(header, i + 1, 0); // (col, row)
        }

        // 2. Créer les en-têtes (Lignes)
        for (int i = 0; i < cities.size(); i++) {
            String city = cities.get(i);
            Label header = new Label(city);
            header.setStyle("-fx-font-weight: bold;");
            matrixGrid.add(header, 0, i + 1); // (col, row)
        }

        // 3. Remplir les données
        if (matrix != null) {
            for (int i = 0; i < cities.size(); i++) { // Ligne (source 'r')
                String sourceCity = cities.get(i);
                for (int j = 0; j
                        < cities.size(); j++) { // Colonne (dest 'c')
                    String destCity = cities.get(j);

                    double dist = matrix.get(sourceCity).get(destCity);
                    String distStr = (dist == Double.POSITIVE_INFINITY) ? "Inf" : String.format("%.0f", dist);

                    Label data = new Label(distStr);
                    matrixGrid.add(data, j + 1, i + 1);
                }
            }
        }
    }

    public BorderPane getView() {
        return view;
    }
}