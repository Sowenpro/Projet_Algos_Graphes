package ui.AlgoView;

import algorithms.Kruskal;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Edge;
import models.Graph;
import models.GraphBuilder;
import ui.AlgoMenu;
import ui.GraphDisplay;

import java.util.List;

public class KruskalView {

    private final BorderPane view;
    private final GraphDisplay graphDisplay;
    private final Graph roadNetwork;
    private final Label costLabel;

    public KruskalView(Stage stage) {
        this.roadNetwork = GraphBuilder.createRoadNetwork();
        this.graphDisplay = new GraphDisplay(roadNetwork);

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        Button runButton = new Button("Calculer l'Arbre (Kruskal)");
        Button resetButton = new Button("Réinitialiser");
        Button backButton = new Button("⬅ Retour");
        backButton.setStyle("-fx-text-fill: red;");

        costLabel = new Label("Coût total : 0.0");
        costLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 0 20;");

        controls.getChildren().addAll(backButton, runButton, resetButton, costLabel);

        // --- Actions ---

        backButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            stage.setScene(new Scene(algoMenu.getView(), 600, 400));
        });

        resetButton.setOnAction(e -> {
            graphDisplay.resetDisplay();
            costLabel.setText("Coût total : 0.0");
        });

        runButton.setOnAction(e -> {
            // 1. Réinitialiser l'affichage
            graphDisplay.resetDisplay();

            // 2. Appeler VOTRE algorithme du module Back
            List<Edge> mstEdges = Kruskal.traverse(roadNetwork);

            // 3. Calculer le coût total (car il n'est pas retourné par la méthode)
            double totalCost = 0;
            for (Edge edge : mstEdges) {
                totalCost += edge.getWeight();
            }

            // 4. Mettre à jour l'affichage
            graphDisplay.highlightTree(mstEdges, Color.GREEN);

            // 5. Mettre à jour le label du coût
            costLabel.setText(String.format("Coût total : %.0f", totalCost));
        });
        // -----------------------------------------------------------------

        view = new BorderPane();
        view.setTop(controls);
        view.setCenter(graphDisplay);
    }

    public BorderPane getView() {
        return view;
    }
}