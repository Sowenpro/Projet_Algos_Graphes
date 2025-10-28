package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu(stage);
        Scene scene = new Scene(mainMenu.getView(), 600, 400);

        stage.setTitle("Projet Graphes - CARDON & BILQUEZ-CYRILLE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
