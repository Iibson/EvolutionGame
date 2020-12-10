package EvolutionGame.app;

import EvolutionGame.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MapController {
    @FXML
    private Button stopButton;

    public void stop() throws IOException {
        App.setRoot("PaneWindow");
    }
}
