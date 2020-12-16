package EvolutionGame.map;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;

import java.util.List;
import java.util.Map;

public interface IWorldMap {

    //    Map<Vector2d, Plant> getPlants();
//
//    Vector2d getJungleBounds();
//
//    Map<Vector2d, Set<Animal>> getAnimals();
//
    Integer getAnimalStartingEnergy();

    int getCurrentNumberOfAnimals();

    int getCurrentNumberOfPlants();

    void addPlants();

    void moveAnimals();

    void positionChanged(Animal animal, Vector2d oldPosition);

    void eatPlants();

    void reproduceAnimals();

    void spentYear();

    boolean checkBounds(Vector2d position);

    void place(Animal animal);

    Animal getAnimal(Vector2d vector2d);

    double getAverageEnergy();

    double getAverageYears();

    double getAverageOffspringsNumber();

    List<Integer> getCurrentDominantGenes();
}