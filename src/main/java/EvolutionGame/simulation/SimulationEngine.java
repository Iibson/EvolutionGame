package EvolutionGame.simulation;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.map.WorldMap;
import EvolutionGame.mapElement.animal.Animal;

import java.util.*;

public class SimulationEngine {

    private IWorldMap world;
    private int year;

    public SimulationEngine(Integer width, Integer height, Integer jungleWidth, Integer jungleHeight, Integer startEnergy, Integer moveEnergy, Integer plantEnergy, int plantSpawnRatio, int numberOfStartingAnimals) {
        this.world = new WorldMap(width, height, jungleWidth, jungleHeight, plantEnergy, startEnergy, plantSpawnRatio, moveEnergy);
        Random random = new Random();
        List<Integer> genes = new ArrayList<>();
        this.year = 0;
        for (int i = 0; i < 9; i++) {
            genes.add(i);
            genes.add(i);
            genes.add(i);
            genes.add(i);
        }
        for (int i = 0; i < numberOfStartingAnimals; i++)
            new Animal(world, new Vector2d(random.nextInt(width) - width / 2 - 1, random.nextInt(height) - height / 2 - 1), MapDirection.NORTH, genes, startEnergy);
    }

    public void simulateAYear() {
        System.out.println(this.year + " " + this.world.getCurrentNumberOfAnimals() + " " + this.world.getCurrentNumberOfPlants());
        world.spentYear();
        this.year++;
    }
}