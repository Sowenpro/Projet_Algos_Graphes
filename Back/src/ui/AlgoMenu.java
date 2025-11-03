package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.AlgoView.*;

public class AlgoMenu {

    private final VBox view;

    public AlgoMenu(Stage stage) {
        Text title = new Text("Choisissez un algorithme");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Boutons pour les algos
        Button bfsBtn = new Button("BFS");
        Button dfsBtn = new Button("DFS");
        Button primBtn = new Button("Prim");
        Button kruskalBtn = new Button("Kruskal");
        Button dijkstraBtn = new Button("Dijkstra");
        Button bellmanBtn = new Button("Bellman-Ford");
        Button floydBtn = new Button("Floyd-Warshall");
        Button backBtn = new Button("⬅ Retour");

        // Style uniforme
        for (Button b : new Button[]{bfsBtn, dfsBtn, primBtn, kruskalBtn, dijkstraBtn, bellmanBtn, floydBtn}) {
            b.setStyle("-fx-pref-width: 180; -fx-font-size: 14px;");
        }
        backBtn.setStyle("-fx-font-size: 13px; -fx-text-fill: red;");

        // Actions
        backBtn.setOnAction(e -> {
            MainMenu mainMenu = new MainMenu(stage);
            stage.setScene(new Scene(mainMenu.getView(), 600, 400));
        });

        bfsBtn.setOnAction(e -> {
            BFSView bfsView = new BFSView(stage);
            Scene scene = new Scene(bfsView.getView(), 800, 700);
            stage.setScene(scene);
        });

        dfsBtn.setOnAction(e -> {
            DFSView dfsView = new DFSView(stage);
            Scene scene = new Scene(dfsView.getView(), 800, 700);
            stage.setScene(scene);
        });

        primBtn.setOnAction(e -> {
            PrimView primView = new PrimView(stage);
            Scene scene = new Scene(primView.getView(), 800, 700);
            stage.setScene(scene);
        });

        kruskalBtn.setOnAction(e -> {
            KruskalView kruskalView = new KruskalView(stage);
            Scene scene = new Scene(kruskalView.getView(), 800, 700);
            stage.setScene(scene);
        });

        dijkstraBtn.setOnAction(e -> {
            DijkstraView dijkstraView = new DijkstraView(stage);
            Scene scene = new Scene(dijkstraView.getView(), 800, 700);
            stage.setScene(scene);
        });

        bellmanBtn.setOnAction(e -> {
            BellmanFordView bellmanFordView = new BellmanFordView(stage);
            Scene scene = new Scene(bellmanFordView.getView(), 800, 700);
            stage.setScene(scene);
        });

        floydBtn.setOnAction(e -> {
            FloydWarshallView floydView = new FloydWarshallView(stage);
            Scene scene = new Scene(floydView.getView(), 600, 500);
            stage.setScene(scene);
        });

        // Organisation
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(15);

        grid.addRow(0, bfsBtn, dfsBtn);
        grid.addRow(1, primBtn, kruskalBtn);
        grid.addRow(2, dijkstraBtn, bellmanBtn);
        grid.addRow(3, floydBtn);

        view = new VBox(25, title, grid, backBtn);
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #f1f3f6;");
    }

    public VBox getView() {
        return view;
    }
}
