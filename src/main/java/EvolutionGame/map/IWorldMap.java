package EvolutionGame.map;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IMapElement;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;

import java.util.Map;
import java.util.Set;

public interface IWorldMap {

    void place(Animal animal);

//    Map<Vector2d, Plant> getPlants();
//
//    Vector2d getJungleBounds();
//
//    Map<Vector2d, Set<Animal>> getAnimals();
//
    Integer getAnimalStartingEnergy();

    int getCurrentNumberOfAnimals();

    void addPlants();

    void moveAnimals();

    void positionChanged(Animal animal, Vector2d oldPosition);

    void eatPlants();

    void reproduceAnimals();

    void removedFromMap(Vector2d position, IMapElement element);

    void spentYear();
}