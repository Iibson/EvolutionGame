module EvolutionGame {
    requires javafx.controls;
    requires javafx.fxml;

    opens EvolutionGame to javafx.fxml;
    exports EvolutionGame;
}