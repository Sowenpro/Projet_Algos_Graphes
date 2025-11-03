package ui.AlgoView;

import algorithms.Prim;

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
import models.Node;
import models.Graph;
import models.GraphBuilder;
import ui.AlgoMenu;
import ui.GraphDisplay;

import java.util.List;
import java.util.Map;

public class PrimView {

    private final BorderPane view;
    private final GraphDisplay graphDisplay;
    private final Graph roadNetwork;
    private final Label costLabel;

    public PrimView(Stage stage) {
        this.roadNetwork = GraphBuilder.createRoadNetwork();
        this.graphDisplay = new GraphDisplay(roadNetwork);

        HBox controls = new HBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        Button runButton = new Button("Calculer l'Arbre (Prim)");
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

            // 2. Appeler l'algorithme Prim
            // Ici on choisit arbitrairement le premier nœud comme point de départ
            Node firstNode = roadNetwork.getNodes().iterator().next();
            String startNodeId = firstNode.getId();

            Map<String, Object> result = Prim.prim(roadNetwork, startNodeId);

            // 3. Récupérer les arêtes et le coût total
            List<Edge> mstEdges = (List<Edge>) result.get("edges");
            double totalCost = (double) result.getOrDefault("totalWeight", 0.0);

            // 4. Mettre à jour l'affichage
            graphDisplay.highlightTree(mstEdges, Color.BLUE);

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
