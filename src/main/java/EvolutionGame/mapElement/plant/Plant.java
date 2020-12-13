package EvolutionGame.mapElement.plant;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IElementObserver;

import java.util.ArrayList;
import java.util.List;

public class Plant{
    private Vector2d position;
    private List<IElementObserver> observers;
    private boolean beingEaten;

    public Plant(Vector2d position) {
        this.position = position;
        observers = new ArrayList<>();
        this.beingEaten = false;
    }

    public boolean isBeingEaten() {
        return this.beingEaten;
    }

    public void setBeingEaten(boolean status) {
        this.beingEaten = status;
    }

    public void addObserver(IElementObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IElementObserver observer) {
        observers.remove(observer);
    }

    public Vector2d getPosition() {
        return this.position;
    }
}
