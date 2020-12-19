package EvolutionGame.map;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.animal.Animal;
import java.util.List;
import java.util.Set;

public interface IWorldMap {

    Integer getAnimalStartingEnergy();

    int getCurrentNumberOfAnimals();

    int getCurrentNumberOfPlants();

    void addPlants();

    void moveAnimals();

    void eatPlants();

    void reproduceAnimals();

    void spentYear();

    boolean checkBounds(Vector2d position);

    List<Integer> getCurrentDominantGenesThroughAllYears();

    void place(Animal animal);

    Animal getAnimal(Vector2d vector2d);

    double getAverageEnergy();

    double getAverageYears();

    double getAverageOffspringsNumber();

    List<Integer> getCurrentDominantGenes();

    Set<Animal> getAnimalsFromPosition(Vector2d position);

    Vector2d generateFreePositionNear(Vector2d position);
}