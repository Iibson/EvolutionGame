package EvolutionGame.map;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.animal.Animal;

public interface IElementObserver {

    void positionChanged(Animal animal, Vector2d oldPosition);

    void removedFromMap(Vector2d position, Animal element);

    boolean checkBounds(Vector2d position);
}
