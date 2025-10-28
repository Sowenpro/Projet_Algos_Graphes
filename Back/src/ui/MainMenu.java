package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu {

    private final VBox view;

    public MainMenu(Stage stage) {
        Text title = new Text("Projet Algorithmes de Graphes");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button showGraphButton = new Button("Afficher le graphe");
        Button algoMenuButton = new Button("Algorithmes disponibles");

        showGraphButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 200;");
        algoMenuButton.setStyle("-fx-font-size: 14px; -fx-pref-width: 200;");

        // Actions
        showGraphButton.setOnAction(e -> {
            System.out.println("Affichage du graphe (à implémenter)");
            // TODO : ouvrir une scène ou un Canvas affichant le graphe
        });

        algoMenuButton.setOnAction(e -> {
            AlgoMenu algoMenu = new AlgoMenu(stage);
            Scene scene = new Scene(algoMenu.getView(), 600, 400);
            stage.setScene(scene);
        });

        view = new VBox(20, title, showGraphButton, algoMenuButton);
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #f8f9fa;");
    }

    public VBox getView() {
        return view;
    }
}
