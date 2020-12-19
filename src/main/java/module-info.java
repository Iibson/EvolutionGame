module EvolutionGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;

    opens EvolutionGame to javafx.fxml;
    opens EvolutionGame.simulation;
    exports EvolutionGame.simulation;
    exports EvolutionGame;
}