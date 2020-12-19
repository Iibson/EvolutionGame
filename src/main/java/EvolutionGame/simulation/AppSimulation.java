package EvolutionGame.simulation;

import EvolutionGame.data.Vector2d;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.InputStreamReader;
import java.util.Objects;

public class AppSimulation implements EventHandler {
    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private boolean engine1Running = true;
    private boolean engine2Running = true;
    private Button stopEngine1 = new Button();
    private Button stopEngine2 = new Button();
    private Button dominantGenes = new Button();
    private boolean isRunning;
    private Button startSimulation = new Button();
    private ISimulationObserver observer;
    private Label engine1Info = new Label();
    private Label engine2Info = new Label();
    private Label engine1ChosenAnimal = new Label();
    private Label engine2ChosenAnimal = new Label();
    private Button setNumberOfYearsChosenAnimal1 = new Button();
    private Button setNumberOfYearsChosenAnimal2 = new Button();
    private TextField numberOfYearsChosenAnimal1 = new TextField();
    private TextField numberOfYearsChosenAnimal2 = new TextField();
    private int n1 = 100;
    private int n2 = 100;

    public AppSimulation(ISimulationObserver observer) {

        this.observer = observer;
        stopEngine1.setOnAction(this);
        stopEngine1.setVisible(false);
        stopEngine1.setText("pause 1st simulation");
        stopEngine1.setLayoutX(10);
        stopEngine1.setLayoutY(740);
        stopEngine2.setOnAction(this);
        stopEngine2.setVisible(false);
        stopEngine2.setText("pause 2nd simulation");
        stopEngine2.setLayoutX(10);
        stopEngine2.setLayoutY(780);
        dominantGenes.setOnAction(this);
        dominantGenes.setVisible(false);
        dominantGenes.setText("show current dominant genes");
        dominantGenes.setLayoutX(10);
        dominantGenes.setLayoutY(820);

        this.isRunning = false;

        startSimulation.setOnAction(this);
        startSimulation.setVisible(true);
        startSimulation.setText("startSimulation");
        startSimulation.setLayoutX(1300);
        startSimulation.setLayoutY(570);

        this.engine1Info.setLayoutX(10);
        this.engine1Info.setLayoutY(550);
        this.engine1Info.setVisible(false);
        this.engine2Info.setLayoutX(620);
        this.engine2Info.setLayoutY(550);
        this.engine2Info.setVisible(false);

        this.engine1ChosenAnimal.setLayoutX(200);
        this.engine1ChosenAnimal.setLayoutY(740);
        this.engine1ChosenAnimal.setVisible(false);
        this.engine2ChosenAnimal.setLayoutX(200);
        this.engine2ChosenAnimal.setLayoutY(890);
        this.engine2ChosenAnimal.setVisible(false);

        this.setNumberOfYearsChosenAnimal1.setText("set n for 1st simulation");
        this.setNumberOfYearsChosenAnimal1.setVisible(false);
        this.setNumberOfYearsChosenAnimal1.setOnAction(this);
        this.setNumberOfYearsChosenAnimal1.setLayoutX(620);
        this.setNumberOfYearsChosenAnimal1.setLayoutY(740);
        this.setNumberOfYearsChosenAnimal2.setText("set n for 2nd simulation");
        this.setNumberOfYearsChosenAnimal2.setVisible(false);
        this.setNumberOfYearsChosenAnimal2.setOnAction(this);
        this.setNumberOfYearsChosenAnimal2.setLayoutX(620);
        this.setNumberOfYearsChosenAnimal2.setLayoutY(780);

        this.numberOfYearsChosenAnimal1.setVisible(false);
        this.numberOfYearsChosenAnimal2.setVisible(false);
        this.numberOfYearsChosenAnimal2.setText("100");
        this.numberOfYearsChosenAnimal1.setText("100");
        this.numberOfYearsChosenAnimal1.setLayoutX(800);
        this.numberOfYearsChosenAnimal1.setLayoutY(740);
        this.numberOfYearsChosenAnimal2.setLayoutX(800);
        this.numberOfYearsChosenAnimal2.setLayoutY(780);
    }

    public void simulate() {
        if (!isRunning)
            return;
        if (engine1Running) {
            engine1.simulateAYear();
            engine1Info.setText(engine1.getInfo());
            engine1ChosenAnimal.setText(engine1.getInfoAboutChosenAnimal(n1));
        }
        if (engine2Running) {
            engine2.simulateAYear();
            engine2Info.setText(engine2.getInfo());
            engine2ChosenAnimal.setText(engine2.getInfoAboutChosenAnimal(n2));
        }
    }

    public Group draw() {
        Group group = new Group();
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
        group.getChildren().add(startSimulation);
        group.getChildren().add(setNumberOfYearsChosenAnimal1);
        group.getChildren().add(setNumberOfYearsChosenAnimal2);
        group.getChildren().add(numberOfYearsChosenAnimal2);
        group.getChildren().add(numberOfYearsChosenAnimal1);
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
            start();
            engine1Running = true;
            engine2Running = true;
        }
        if(event.getSource() == setNumberOfYearsChosenAnimal1)
            new Thread(() -> n1 = Integer.parseInt(numberOfYearsChosenAnimal1.getText())).start();
        if(event.getSource() == setNumberOfYearsChosenAnimal2)
            new Thread(() -> n2 = Integer.parseInt(numberOfYearsChosenAnimal2.getText())).start();
    }

    private void start() {
        JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("parameters.json")))).getAsJsonObject();
        engine1 = new SimulationEngine(
                jsonObject.get("width").getAsInt(),
                jsonObject.get("height").getAsInt(),
                jsonObject.get("jungleWidth").getAsInt(),
                jsonObject.get("jungleHeight").getAsInt(),
                jsonObject.get("startEnergy").getAsInt(),
                jsonObject.get("moveEnergy").getAsInt(),
                jsonObject.get("plantEnergy").getAsInt(),
                jsonObject.get("plantSpawnRatio").getAsInt(),
                jsonObject.get("startingAnimals").getAsInt(),
                0,
                1000

        );
        engine2 = new SimulationEngine(
                jsonObject.get("width").getAsInt(),
                jsonObject.get("height").getAsInt(),
                jsonObject.get("jungleWidth").getAsInt(),
                jsonObject.get("jungleHeight").getAsInt(),
                jsonObject.get("startEnergy").getAsInt(),
                jsonObject.get("moveEnergy").getAsInt(),
                jsonObject.get("plantEnergy").getAsInt(),
                jsonObject.get("plantSpawnRatio").getAsInt(),
                jsonObject.get("startingAnimals").getAsInt(),
                600,
                1000

        );
        startSimulation.setText("restartSimulation");
        stopEngine1.setVisible(true);
        stopEngine2.setVisible(true);
        dominantGenes.setVisible(true);
        engine1Info.setVisible(true);
        engine2Info.setVisible(true);
        engine1ChosenAnimal.setVisible(true);
        engine2ChosenAnimal.setVisible(true);
        this.setNumberOfYearsChosenAnimal2.setVisible(true);
        this.setNumberOfYearsChosenAnimal1.setVisible(true);
        this.numberOfYearsChosenAnimal1.setVisible(true);
        this.numberOfYearsChosenAnimal2.setVisible(true);
        isRunning = true;
        changedSimulation();
    }

    private void changedSimulation() {
        this.observer.changedSimulation();
    }


}
