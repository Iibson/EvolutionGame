package EvolutionGame.simulation;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.map.WorldMap;
import EvolutionGame.map.visualisation.Visualiser;
import EvolutionGame.mapElement.animal.Animal;

import java.util.*;

public class SimulationEngine {

    private IWorldMap world;
    private int year;
    public Visualiser visualiser;

    public SimulationEngine(Integer width, Integer height, Integer jungleWidth, Integer jungleHeight, Integer startEnergy, Integer moveEnergy, Integer plantEnergy, int plantSpawnRatio, int numberOfStartingAnimals) {
        Vector2d mapBounds = new Vector2d((width / 2), (height / 2));
        Vector2d jungleBounds = new Vector2d((jungleWidth / 2), (jungleHeight / 2));
        visualiser = new Visualiser(mapBounds, jungleBounds);
        this.world = new WorldMap(mapBounds, jungleBounds, plantEnergy, startEnergy, plantSpawnRatio, moveEnergy, visualiser);
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
            new Animal(world, new Vector2d(random.nextInt(width) - width / 2, random.nextInt(height) - height / 2), MapDirection.NORTH, genes, startEnergy);
    }

    public void simulateAYear() {
        world.spentYear();
//        System.out.println(this.year + " " + this.world.getCurrentNumberOfAnimals() + " " + this.world.getCurrentNumberOfPlants());
        this.year++;
    }
}