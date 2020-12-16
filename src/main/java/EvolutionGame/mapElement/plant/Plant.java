package EvolutionGame.mapElement.plant;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IElementObserver;

import java.util.ArrayList;
import java.util.List;

public class Plant{
    private Vector2d position;
    private boolean beingEaten;

    public Plant(Vector2d position) {
        this.position = position;
        this.beingEaten = false;
    }

    public boolean isBeingEaten() {
        return this.beingEaten;
    }

    public void setBeingEaten(boolean status) {
        this.beingEaten = status;
    }


    public Vector2d getPosition() {
        return this.position;
    }
}
