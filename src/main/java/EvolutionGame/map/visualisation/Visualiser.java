package EvolutionGame.map.visualisation;

import EvolutionGame.data.Vector2d;
import EvolutionGame.map.IElementObserver;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Visualiser implements IElementObserver {

    private Map<Vector2d, Rectangle> mapElements;
    private Vector2d jungleBounds;

    public Visualiser(Vector2d mapBounds, Vector2d jungleBounds) {
        this.jungleBounds = jungleBounds;
        this.mapElements = new HashMap<>();
        new ArrayList<>(mapBounds.opposite().square(mapBounds)).forEach(element -> {
            Rectangle rectangle = new Rectangle((element.x + mapBounds.x) * 10, (element.y + mapBounds.y) * 10, 10, 10);
            rectangle.setFill(Color.SANDYBROWN);
            rectangle.setStroke(Color.TRANSPARENT);
            mapElements.put(element, rectangle);
        });
        new ArrayList<>(jungleBounds.opposite().square(jungleBounds)).forEach(element -> mapElements.get(element).setFill(Color.GREEN));
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition) {
        if (oldPosition.follows(jungleBounds.opposite()) && oldPosition.precedes(jungleBounds))
            mapElements.get(oldPosition).setFill(Color.GREEN);
        else
            mapElements.get(oldPosition).setFill(Color.SANDYBROWN);
        mapElements.get(animal.getPosition()).setFill(Color.BLACK);
    }

    @Override
    public void removedFromMap(Vector2d position, Animal element) {
        if (position.follows(jungleBounds.opposite()) && position.precedes(jungleBounds))
            mapElements.get(position).setFill(Color.GREEN);
        else
            mapElements.get(position).setFill(Color.SANDYBROWN);

    }

    @Override
    public void place(Animal animal) {
        this.mapElements.get(animal.getPosition()).setFill(Color.BLACK);
    }

    public void addPlant(Plant plant) {
        this.mapElements.get(plant.getPosition()).setFill(Color.LIGHTGREEN);
    }

    public Group draw() {
        Group group = new Group();
        for (Vector2d vector2d : mapElements.keySet())
            group.getChildren().add(mapElements.get(vector2d));
        return group;
    }
}
