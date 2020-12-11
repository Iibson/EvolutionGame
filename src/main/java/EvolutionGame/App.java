package EvolutionGame;

import EvolutionGame.simulation.AppSimulation;
import EvolutionGame.simulation.SimulationEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage){
        AppSimulation engine = new AppSimulation(100, 100, 30, 30, 10, 1, 10, 10, 300);
        scene = new Scene(engine.draw(), 1200, 1200);
        stage.setScene(scene);
        stage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80), event -> engine.simulate()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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