package EvolutionGame.simulation;

import EvolutionGame.data.Vector2d;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AppSimulation implements EventHandler {
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private boolean engine1Running = true;
    private boolean engine2Running = true;
    private Button stopEngine1 = new Button();
    private Button stopEngine2 = new Button();
    private Button dominantGenes = new Button();
    private boolean isRunning;
    private TextField startingAnimals = new TextField();
    private TextField plantSpawnRatio = new TextField();
    private TextField jungleWidth = new TextField();
    private TextField plantEnergy = new TextField();
    private TextField startEnergy = new TextField();
    private TextField jungleHeight = new TextField();
    private TextField moveEnergy = new TextField();
    private Button startSimulation = new Button();
    private TextField height = new TextField();
    private TextField width = new TextField();
    private ISimulationObserver observer;
    private Label engine1Info = new Label();
    private Label engine2Info = new Label();
    private Label engine1ChosenAnimal = new Label();
    private Label engine2ChosenAnimal = new Label();

    public AppSimulation(ISimulationObserver observer) {

        this.observer = observer;
        stopEngine1.setOnAction(this);
        stopEngine1.setVisible(false);
        stopEngine1.setText("pause 1st simulation");
        stopEngine1.setLayoutX(10);
        stopEngine1.setLayoutY(700);
        stopEngine2.setOnAction(this);
        stopEngine2.setVisible(false);
        stopEngine2.setText("pause 2nd simulation");
        stopEngine2.setLayoutX(10);
        stopEngine2.setLayoutY(740);
        dominantGenes.setOnAction(this);
        dominantGenes.setVisible(false);
        dominantGenes.setText("show dominant genes");
        dominantGenes.setLayoutX(10);
        dominantGenes.setLayoutY(780);

        this.isRunning = false;

        startSimulation.setOnAction(this);
        startSimulation.setVisible(true);
        startSimulation.setText("startSimulation");
        startSimulation.setLayoutX(1300);
        startSimulation.setLayoutY(570);

        this.height.setLayoutX(1300);
        this.height.setLayoutY(600);
        this.height.setText("100");
        this.width.setLayoutX(1300);
        this.width.setLayoutY(630);
        this.width.setText("100");
        this.jungleWidth.setLayoutX(1300);
        this.jungleWidth.setLayoutY(690);
        this.jungleWidth.setText("50");
        this.jungleHeight.setLayoutX(1300);
        this.jungleHeight.setLayoutY(660);
        this.jungleHeight.setText("50");
        this.startingAnimals.setLayoutX(1300);
        this.startingAnimals.setLayoutY(720);
        this.startingAnimals.setText("100");
        this.plantSpawnRatio.setLayoutX(1300);
        this.plantSpawnRatio.setLayoutY(750);
        this.plantSpawnRatio.setText("4");
        this.plantEnergy.setLayoutX(1300);
        this.plantEnergy.setLayoutY(780);
        this.plantEnergy.setText("15");
        this.startEnergy.setLayoutX(1300);
        this.startEnergy.setLayoutY(810);
        this.startEnergy.setText("30");
        this.moveEnergy.setLayoutX(1300);
        this.moveEnergy.setLayoutY(840);
        this.moveEnergy.setText("1");

        this.engine1Info.setLayoutX(10);
        this.engine1Info.setLayoutY(550);
        this.engine1Info.setVisible(false);
        this.engine2Info.setLayoutX(620);
        this.engine2Info.setLayoutY(550);
        this.engine2Info.setVisible(false);

        this.engine1ChosenAnimal.setLayoutX(200);
        this.engine1ChosenAnimal.setLayoutY(700);
        this.engine1ChosenAnimal.setVisible(false);
        this.engine2ChosenAnimal.setLayoutX(200);
        this.engine2ChosenAnimal.setLayoutY(800);
        this.engine2ChosenAnimal.setVisible(false);
    }


    public void simulate() {
        if (!isRunning)
            return;
        if (engine1Running) {
            engine1.simulateAYear();
            engine1Info.setText(engine1.getInfo());
            engine1ChosenAnimal.setText(engine1.getInfoAboutChosenAnimal());
        }
        if (engine2Running) {
            engine2.simulateAYear();
            engine2Info.setText(engine2.getInfo());
            engine2ChosenAnimal.setText(engine2.getInfoAboutChosenAnimal());
        }
    }

    public Group draw() {
        Group group = new Group();
        Label label;
        if (isRunning) {
            for (Vector2d v : engine1.draw().keySet())
                group.getChildren().add(engine1.draw().get(v));
            for (Vector2d v : engine2.draw().keySet())
                group.getChildren().add(engine2.draw().get(v));
        }
        group.getChildren().add(stopEngine1);
        group.getChildren().add(stopEngine2);
        group.getChildren().add(dominantGenes);
        group.getChildren().add(engine1Info);
        group.getChildren().add(engine2Info);
        group.getChildren().add(engine1ChosenAnimal);
        group.getChildren().add(engine2ChosenAnimal);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(720);
        label.setText("startingAnimals");
        group.getChildren().add(label);
        group.getChildren().add(startingAnimals);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(750);
        label.setText("plantSpawnRatio");
        group.getChildren().add(label);
        group.getChildren().add(plantSpawnRatio);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(660);
        label.setText("jungleHeight");
        group.getChildren().add(label);
        group.getChildren().add(jungleHeight);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(690);
        label.setText("jungleWidth");
        group.getChildren().add(label);
        group.getChildren().add(jungleWidth);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(630);
        label.setText("width");
        group.getChildren().add(label);
        group.getChildren().add(width);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(600);
        label.setText("height");
        group.getChildren().add(label);
        group.getChildren().add(height);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(840);
        label.setText("moveEnergy");
        group.getChildren().add(label);
        group.getChildren().add(moveEnergy);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(780);
        label.setText("plantEnergy");
        group.getChildren().add(label);
        group.getChildren().add(plantEnergy);
        label = new Label();
        label.setLayoutX(1200);
        label.setLayoutY(810);
        label.setText("startEnergy");
        group.getChildren().add(label);
        group.getChildren().add(startEnergy);
        group.getChildren().add(startSimulation);
        return group;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == stopEngine1)
            new Thread(() -> engine1Running = !engine1Running).start();
        if (event.getSource() == stopEngine2)
            new Thread(() -> engine2Running = !engine2Running).start();
        if (event.getSource() == dominantGenes) {
            engine1.showDominantGenes();
            engine2.showDominantGenes();
        }
        if (event.getSource() == startSimulation) {
            try {
                start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            engine1Running = true;
            engine2Running = true;
        }

    }

    private void clear() {
        if (!isRunning)
            return;
        engine1.clearVisualiser();
        engine2.clearVisualiser();
        engine1 = null;
        engine2 = null;
        isRunning = false;
        changedSimulation();
        stopEngine1.setVisible(false);
        stopEngine2.setVisible(false);
        dominantGenes.setVisible(false);
        engine1Info.setVisible(false);
        engine2Info.setVisible(false);
        engine1ChosenAnimal.setVisible(false);
        engine2ChosenAnimal.setVisible(false);
    }

    private void start() throws IOException {
        engine1 = new SimulationEngine(Integer.parseInt(
                width.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(jungleWidth.getText()),
                Integer.parseInt(jungleHeight.getText()),
                Integer.parseInt(startEnergy.getText()),
                Integer.parseInt(moveEnergy.getText()),
                Integer.parseInt(plantEnergy.getText()),
                Integer.parseInt(plantSpawnRatio.getText()),
                Integer.parseInt(startingAnimals.getText()),
                0,
                1000

        );
        engine2 = new SimulationEngine(Integer.parseInt(
                width.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(jungleWidth.getText()),
                Integer.parseInt(jungleHeight.getText()),
                Integer.parseInt(startEnergy.getText()),
                Integer.parseInt(moveEnergy.getText()),
                Integer.parseInt(plantEnergy.getText()),
                Integer.parseInt(plantSpawnRatio.getText()),
                Integer.parseInt(startingAnimals.getText()),
                600,
                1000

        );
        stopEngine1.setVisible(true);
        stopEngine2.setVisible(true);
        dominantGenes.setVisible(true);
        engine1Info.setVisible(true);
        engine2Info.setVisible(true);
        engine1ChosenAnimal.setVisible(true);
        engine2ChosenAnimal.setVisible(true);
        isRunning = true;
        changedSimulation();
    }

    private void changedSimulation() {
        this.observer.changedSimulation();
    }


}
