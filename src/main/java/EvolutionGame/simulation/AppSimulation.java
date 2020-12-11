package EvolutionGame.simulation;

import EvolutionGame.data.Vector2d;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class AppSimulation implements EventHandler {
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private boolean engine1Running = true;
    private boolean engine2Running = true;
    private Button stopEngine1 = new Button();
    private Button stopEngine2 = new Button();

    public AppSimulation(Integer width, Integer height, Integer jungleWidth, Integer jungleHeight, Integer startEnergy, Integer moveEnergy, Integer plantEnergy, int plantSpawnRatio, int numberOfStartingAnimals) {
        engine1 = new SimulationEngine(width, height, jungleWidth, jungleHeight, startEnergy, moveEnergy, plantEnergy, plantSpawnRatio, numberOfStartingAnimals, 0);
        engine2 = new SimulationEngine(50, 50, jungleWidth, jungleHeight, startEnergy, moveEnergy, plantEnergy, plantSpawnRatio, numberOfStartingAnimals, 510);
        stopEngine1.setOnAction(this);
        stopEngine1.setText("pause 1st simulation");
        stopEngine1.setLayoutX(600);
        stopEngine1.setLayoutY(100);
        stopEngine2.setOnAction(this);
        stopEngine2.setText("pause 2nd simulation");
        stopEngine2.setLayoutX(600);
        stopEngine2.setLayoutY(610);
    }


    public void simulate() {
        if (engine1Running)
            engine1.simulateAYear();
        if (engine2Running)
            engine2.simulateAYear();
    }

    public Group draw() {
        Group group = new Group();
        for (Vector2d v : engine1.mapVisualiser.draw().keySet())
            group.getChildren().add(engine1.mapVisualiser.draw().get(v));
        for (Vector2d v : engine2.mapVisualiser.draw().keySet())
            group.getChildren().add(engine2.mapVisualiser.draw().get(v));
        group.getChildren().add(stopEngine1);
        group.getChildren().add(stopEngine2);
        return group;
    }

    public void setEngine1Running(boolean engine1Running) {
        this.engine1Running = engine1Running;
    }

    public void setEngine2Running(boolean engine2Running) {
        this.engine2Running = engine2Running;
    }

    public boolean isEngine1Running() {
        return engine1Running;
    }

    public boolean isEngine2Running() {
        return engine2Running;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == stopEngine1)
            new Thread(() -> engine1Running = !engine1Running).start();
        if (event.getSource() == stopEngine2)
            new Thread(() -> engine2Running = !engine2Running).start();

    }
}
