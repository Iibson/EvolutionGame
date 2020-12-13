package EvolutionGame.simulation;

import org.junit.Test;

import java.io.IOException;


public class SimulationEngineTest {

    @Test
    public void testRun(){
        new SimulationEngine(10, 10, 6, 6, 20, 1, 10, 2, 10, 0, 100000).simulateAYear();
    }
}
