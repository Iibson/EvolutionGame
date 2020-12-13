package EvolutionGame.mapElement;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;

public interface IElementObserver {

    void positionChanged(Animal animal, Vector2d oldPosition);

    void removedFromMap(Vector2d position, Animal element);

    void place(Animal animal);

    void addPlant(Plant plant);
}
