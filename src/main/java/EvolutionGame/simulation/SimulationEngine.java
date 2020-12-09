package EvolutionGame.simulation;

import EvolutionGame.map.IWorldMap;

import java.util.*;

public class SimulationEngine {

    private List<IWorldMap> worlds;
    private int numberOfYearsSimulated;

    SimulationEngine(List<IWorldMap> worlds, int numberOfYearsSimulated) { //TODO
        this.worlds = worlds;
        this.numberOfYearsSimulated = numberOfYearsSimulated;
    }

    public void run() {
        for (int i = 0; i < numberOfYearsSimulated; i++)
            worlds.forEach(IWorldMap::spentYear);
    }
}