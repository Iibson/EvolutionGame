package EvolutionGame;

import EvolutionGame.simulation.AppSimulation;
import EvolutionGame.simulation.ISimulationObserver;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * w pom.xml znajduje się <executable>path/bin/java</executable> program może nie zadziałć bez ustawienia path
 *
 * JavaFX App
 */
public class App extends Application implements ISimulationObserver {

    private static AppSimulation engine;
    private Stage window;

    @Override
    public void start(Stage stage) {
        window = stage;
        engine = new AppSimulation(this);
        Scene scene = new Scene(engine.draw(), 1480, 1000);
        window.setScene(scene);
        window.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> engine.simulate()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void changedSimulation() {
        window.setScene(new Scene(engine.draw(), 1480, 1000));
    }
}