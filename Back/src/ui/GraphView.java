package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Graph; // Import depuis le module Back
import models.GraphBuilder;

public class GraphView {

    private final BorderPane view;

    public GraphView(Stage stage) {
        // 1. Charger les données du graphe depuis le module Back
        Graph roadNetwork = GraphBuilder.createRoadNetwork();

        // 2. Bouton Retour (comme dans AlgoMenu)
        Button backBtn = new Button("⬅ Retour au Menu");
        backBtn.setStyle("-fx-font-size: 13px; -fx-text-fill: red;");
        backBtn.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu(stage);
            stage.setScene(new Scene(mainMenu.getView(), 600, 400));
        });

        // Conteneur pour le bouton, pour l'aligner
        VBox topContainer = new VBox(backBtn);
        topContainer.setPadding(new Insets(10));
        topContainer.setAlignment(Pos.CENTER_LEFT);

        // 3. Créer le composant d'affichage du graphe
        GraphDisplay graphDisplay = new GraphDisplay(roadNetwork);

        // 4. Mettre en page la vue
        view = new BorderPane();
        view.setTop(topContainer);
        view.setCenter(graphDisplay); // L'affichage du graphe va au centre
        view.setStyle("-fx-background-color: #ffffff;");
    }

    public BorderPane getView() {
        return view;
    }
}