module EvolutionGame {
    requires javafx.controls;
    requires javafx.fxml;

    opens EvolutionGame to javafx.fxml;
    opens EvolutionGame.simulation;
    exports EvolutionGame.simulation;
    exports EvolutionGame;
}