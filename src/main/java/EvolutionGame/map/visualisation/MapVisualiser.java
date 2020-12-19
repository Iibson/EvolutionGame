package EvolutionGame.map.visualisation;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IElementObserver;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class MapVisualiser implements IElementObserver {

    private Map<Vector2d, Rectangle> mapElements;
    private Vector2d jungleBounds;
    private IWorldMap map;
    private Rectangle currentlyFollowed;
    private Animal chosenAnimal;
    private int chosenAnimalDeathDate = 0;

    public MapVisualiser(Vector2d mapBounds, Vector2d jungleBounds, int shift) {
        int a = (int) Math.ceil(500 / (mapBounds.x * 2));
        this.jungleBounds = jungleBounds;
        this.mapElements = new HashMap<>();
        new ArrayList<>(mapBounds.opposite().square(mapBounds)).forEach(element -> {
            Rectangle rectangle = new Rectangle((element.x + mapBounds.x) * a + shift + 10, (element.y + mapBounds.y) * a + 10, a, a);
            rectangle.setFill(Color.SANDYBROWN);
            rectangle.setStroke(Color.TRANSPARENT);
            rectangle.setOnMouseClicked(mouseEvent -> {
                if (rectangle.getFill() != Color.SANDYBROWN && rectangle.getFill() != Color.LIGHTGREEN && rectangle.getFill() != Color.GREEN) {
                    if (this.currentlyFollowed != null)
                        removedFromMap(new Vector2d((int) (currentlyFollowed.getX() - shift - 10) / a - mapBounds.x, (int) (currentlyFollowed.getY() - 10) / a - mapBounds.y), null);
                    this.currentlyFollowed = rectangle;
                    currentlyFollowed.setFill(Color.BLUE);
                    chosenAnimal = map.getAnimal(new Vector2d((int) (rectangle.getX() - shift - 10) / a - mapBounds.x, (int) (rectangle.getY() - 10) / a - mapBounds.y));
                    chosenAnimalDeathDate = 0;
                }
            });
            mapElements.put(element, rectangle);
        });
        new ArrayList<>(jungleBounds.opposite().square(jungleBounds)).forEach(element -> mapElements.get(element).setFill(Color.GREEN));
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition) {
        Animal tempAnimal = map.getAnimal(oldPosition);
        if (oldPosition.follows(jungleBounds.opposite()) && oldPosition.precedes(jungleBounds)) {
            if (tempAnimal == null)
                mapElements.get(oldPosition).setFill(Color.GREEN);
            else
                mapElements.get(tempAnimal.getPosition()).setFill(Color.rgb(Math.min(255, tempAnimal.getEnergy()), 0, 0));
        } else {
            if (tempAnimal == null)
                mapElements.get(oldPosition).setFill(Color.SANDYBROWN);
            else
                mapElements.get(tempAnimal.getPosition()).setFill(Color.rgb(Math.min(255, tempAnimal.getEnergy()), 0, 0));
        }
        if (animal.equals(chosenAnimal)) {
            mapElements.get(animal.getPosition()).setFill(Color.BLUE);
            currentlyFollowed = mapElements.get(animal.getPosition());
        } else
            mapElements.get(animal.getPosition()).setFill(Color.rgb(Math.min(255, animal.getEnergy()), 0, 0));

    }

    @Override
    public void removedFromMap(Vector2d position, Animal element) {
        if (mapElements.get(position).getFill() == Color.BLUE)
            currentlyFollowed = null;
        if (position.follows(jungleBounds.opposite()) && position.precedes(jungleBounds))
            mapElements.get(position).setFill(Color.GREEN);
        else
            mapElements.get(position).setFill(Color.SANDYBROWN);
    }

    @Override
    public void place(Animal animal) {
        mapElements.get(animal.getPosition()).setFill(Color.rgb(Math.min(255, animal.getEnergy()), 0, 0));
    }

    public void addPlant(Plant plant) {
        this.mapElements.get(plant.getPosition()).setFill(Color.LIGHTGREEN);
    }

    public Map<Vector2d, Rectangle> draw() {
        return Collections.unmodifiableMap(mapElements);
    }

    public void clearMap() {
        mapElements.keySet().forEach(key -> mapElements.get(key).setFill(Color.WHITE));
        mapElements.clear();
    }

    public void addIWorldMap(IWorldMap map) {
        this.map = map;
    }

    public String getInfoAboutChosenAnimal(int n, int year) {
        if (chosenAnimal == null)
            return "";
        if(chosenAnimal.getEnergy() <= 0 && chosenAnimalDeathDate == 0)
            chosenAnimalDeathDate = year;
        return "Energy: " + chosenAnimal.getEnergy() +
                "\nNumber of Children after n years from birth: " + chosenAnimal.getNumberOfOffspringsAfterNYears(n) +
                "\nNumber of Descendants after n years from birth: " + chosenAnimal.getNumberOfDescendantAfterNYears(new HashSet<>(), n, chosenAnimal.getYears()) +
                "\nYears: " + chosenAnimal.getYears() +
                "\nDied In: " + (chosenAnimal.getEnergy() <= 0 ? chosenAnimalDeathDate  : "Still Alive") +
                "\nGenes: " + chosenAnimal.getGenes().toString();
    }

    public void showDominantGenes() {
        List<Integer> genes = map.getCurrentDominantGenes();
        Animal tempAnimal;
        Iterator iterator;
        for (Vector2d vector2d : mapElements.keySet()) {
            if (mapElements.get(vector2d).getFill() != Color.SANDYBROWN && mapElements.get(vector2d).getFill() != Color.LIGHTGREEN && mapElements.get(vector2d).getFill() != Color.GREEN) {
                iterator = map.getAnimalsFromPosition(vector2d).iterator();
                while (iterator.hasNext()) {
                    tempAnimal = (Animal) iterator.next();
                    if (tempAnimal.getGenes().equals(genes)) {
                        mapElements.get(vector2d).setFill(Color.YELLOW);
                        break;
                    }
                }
            }
        }
    }
}
