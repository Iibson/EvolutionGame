package EvolutionGame;

import EvolutionGame.data.Vector2d;
import EvolutionGame.simulation.SimulationEngine;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage){
        SimulationEngine engine = new SimulationEngine(100, 100, 30, 30, 1000, 1, 15, 4, 5);

        scene = new Scene(engine.visualiser.draw(), 1010, 1010);
        stage.setScene(scene);
        stage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80), event -> engine.simulateAYear()));
        timeline.setCycleCount(1000);
        timeline.play();
//        rectangle.setFill(Color.GREEN);
//        scene = new Scene((loadFXML("PaneWindow")));
//        stage.setScene(scene);
//        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }

}