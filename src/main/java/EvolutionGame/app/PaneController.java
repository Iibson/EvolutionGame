package EvolutionGame.app;

import EvolutionGame.App;
import EvolutionGame.simulation.SimulationEngine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PaneController extends TextField {

    @FXML
    private TextField startingAnimals;
    @FXML
    private TextField plantSpawnRatio;
    @FXML
    private TextField jungleWidth;
    @FXML
    private TextField plantEnergy;
    @FXML
    private TextField startEnergy;
    @FXML
    private TextField jungleHeight;
    @FXML
    private TextField moveEnergy;
    @FXML
    private Button button;
    @FXML
    private Button pauseButton1;
    @FXML
    private Button pauseButton2;
    @FXML
    private TextField width;
    @FXML
    private TextField height;

    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private boolean engine1Running;
    private boolean engine2Running;

    public PaneController() {
        engine1Running = true;
        engine2Running = true;
    }

    @FXML
    void initialize() {
    }

    @FXML
    private void start() throws IOException {
        App.setRoot("Map");
    }

    @FXML
    public void onActionButton1() throws IOException {
        start();
        create();
        new Thread(() -> {
            while (true){
                try {
                    if (engine1Running){
                        engine1.simulateAYear();
//                        System.out.println("1 running");
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (engine2Running){
                        engine2.simulateAYear();
//                        System.out.println("2 running");
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    public void pause1() {
        new Thread(() -> engine1Running = !engine1Running).start();
    }

    @FXML
    public void pause2() {
        new Thread(() -> engine2Running = !engine2Running).start();
    }

    @FXML
    private void create() {
        engine1 = new SimulationEngine(Integer.parseInt(
                width.getText()),
                Integer.parseInt(height.getText()),
                Integer.parseInt(jungleWidth.getText()),
                Integer.parseInt(jungleHeight.getText()),
                Integer.parseInt(startEnergy.getText()),
                Integer.parseInt(moveEnergy.getText()),
                Integer.parseInt(plantEnergy.getText()),
                Integer.parseInt(plantSpawnRatio.getText()),
                Integer.parseInt(startingAnimals.getText())

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
                Integer.parseInt(startingAnimals.getText())

        );
    }
}
