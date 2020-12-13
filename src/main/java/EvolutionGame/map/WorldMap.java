package EvolutionGame.map;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IElementObserver;
import javafx.util.Pair;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements IWorldMap, IElementObserver {
    private final Vector2d mapBounds;
    private final Vector2d jungleBounds;
    private final Integer plantEnergy;
    private final Map<Vector2d, Plant> plants = new LinkedHashMap<>();
    private final Map<Vector2d, Set<Animal>> animals = new LinkedHashMap<>();
    private final List<Plant> eatenPlants = new ArrayList<>();
    private final Integer animalStartingEnergy;
    private int currentNumberOfAnimals = 0;
    private int currentNumberOfPlants = 0;
    private Queue<Vector2d> freeJunglePlants;
    private Queue<Vector2d> freeSteppesPlants;
    private final int plantsSpawnRatio;
    private final Integer moveEnergy;
    private final IElementObserver visualiser;
    private long allAnimalsEnergy = 0;
    private int numberOfDeadAnimals = 0;
    private long allAnimalYearsLived = 0;
    private long numberOfAllOffsprings = 0;

    public WorldMap(Vector2d mapBounds, Vector2d jungleBounds, Integer plantEnergy, Integer animalStartingEnergy, int plantsSpawnRatio, Integer moveEnergy, IElementObserver visualiser) {
        this.moveEnergy = moveEnergy;
        this.mapBounds = mapBounds;
        this.plantEnergy = plantEnergy;
        this.jungleBounds = jungleBounds;
        this.animalStartingEnergy = animalStartingEnergy;
        List<Vector2d> temp = new ArrayList<>(jungleBounds.opposite().square(jungleBounds));
        Collections.shuffle(temp);
        this.freeJunglePlants = new LinkedList<>(temp);
        temp = new ArrayList<>(prepareFreeSteppesPlants());
        Collections.shuffle(temp);
        this.freeSteppesPlants = new LinkedList<>(temp);
        this.plantsSpawnRatio = plantsSpawnRatio;
        this.visualiser = visualiser;
    }

    public int getCurrentNumberOfAnimals() {
        return currentNumberOfAnimals;
    }

    Map<Vector2d, Plant> getPlants() {
        return Collections.unmodifiableMap(this.plants);
    }

    public int getCurrentNumberOfPlants() {
        return this.currentNumberOfPlants;
    }

    public Integer getAnimalStartingEnergy() {
        return this.animalStartingEnergy;
    }

    Vector2d getJungleBounds() {
        return this.jungleBounds;
    }

    Map<Vector2d, Set<Animal>> getAnimals() {
        return Collections.unmodifiableMap(this.animals);
    }

    public boolean checkBounds(Vector2d position) {
        return !this.mapBounds.opposite().precedes(position) || !this.mapBounds.follows(position);
    }

    private boolean isInJungle(Vector2d position) {
        return position.follows(jungleBounds.opposite()) && position.precedes(jungleBounds);
    }

    public void addPlants() {
        List<Vector2d> temp = new ArrayList<>(this.freeJunglePlants);
        Collections.shuffle(temp);
        if (this.freeJunglePlants.size() != 0)
            this.freeJunglePlants = new LinkedList<>(temp);
        temp = new ArrayList<>(this.freeSteppesPlants);
        Collections.shuffle(temp);
        if (this.freeSteppesPlants.size() != 0)
            this.freeSteppesPlants = new LinkedList<>(temp);
        addPlantsToJungle();
        addPlantsToSteppes();
    }

    private Queue<Vector2d> prepareFreeSteppesPlants() {
        return mapBounds
                .opposite()
                .square(mapBounds)
                .stream()
                .filter(vector2d -> !freeJunglePlants.contains(vector2d))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void addPlantsToSteppes() {
        if (freeSteppesPlants.size() <= 5)
            return;
        Vector2d v = freeSteppesPlants.poll();
        for (int i = 0; i < freeSteppesPlants.size() && !(animals.get(v) == null); i++) {
            freeSteppesPlants.add(v);
            v = freeSteppesPlants.poll();
        }
        Plant tempPlant = new Plant(v);
        tempPlant.addObserver(this);
        tempPlant.addObserver(visualiser);
        visualiser.addPlant(tempPlant);
        addPlant(tempPlant);
        currentNumberOfPlants += 1;
    }

    private void addPlantsToJungle() {
        if (freeJunglePlants.size() <= 5)
            return;
        Vector2d v = freeJunglePlants.poll();
        for (int i = 0; i < freeJunglePlants.size() && !(animals.get(v) == null); i++) {
            freeJunglePlants.add(v);
            v = freeJunglePlants.poll();
        }
        Plant tempPlant = new Plant(v);
        tempPlant.addObserver(this);
        tempPlant.addObserver(visualiser);
        visualiser.addPlant(tempPlant);
        addPlant(tempPlant);
        currentNumberOfPlants += 1;
    }

    public void moveAnimals() {
        animals.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList())
                .forEach(animal -> {
                    if(animal.move()){
                        this.allAnimalYearsLived++;
                        animal.subtractEnergy(this.moveEnergy);
                        this.allAnimalsEnergy -= this.moveEnergy;
                    }
                });
    }

    public void positionChanged(Animal animal, Vector2d oldPosition) {
        Set<Animal> tempAnimalSet = animals.get(oldPosition);
        tempAnimalSet.remove(animal);
        if (tempAnimalSet.isEmpty())
            animals.remove(oldPosition);
        tempAnimalSet = animals.get(animal.getPosition());
        if (tempAnimalSet == null)
            tempAnimalSet = new LinkedHashSet<>();
        tempAnimalSet.add(animal);
        animals.put(animal.getPosition(), tempAnimalSet);
        if (plants.get(animal.getPosition()) != null && !plants.get(animal.getPosition()).isBeingEaten()) {
            eatenPlants.add(plants.get(animal.getPosition()));
            plants.get(animal.getPosition()).setBeingEaten(true);
        }
    }

    public void place(Animal animal) {
        this.currentNumberOfAnimals++;
        animal.addObserver(this);
        animal.addObserver(visualiser);
        visualiser.place(animal);
        Set<Animal> tempAnimalSet = animals.get(animal.getPosition());
        if (tempAnimalSet == null)
            tempAnimalSet = new LinkedHashSet<>();
        tempAnimalSet.add(animal);
        animals.put(animal.getPosition(), tempAnimalSet);
        this.allAnimalsEnergy += animal.getEnergy();
    }

    @Override
    public void addPlant(Plant plant) {
        this.plants.put(plant.getPosition(), plant);
    }

    public void eatPlants() {
        for (Plant plant : eatenPlants) {
            currentNumberOfPlants--;
            eatPlant(plant, animals.get(plant.getPosition()));
            if (isInJungle(plant.getPosition()))
                freeJunglePlants.add(plant.getPosition());
            else
                freeSteppesPlants.add(plant.getPosition());
        }
        eatenPlants.clear();
    }

    private void eatPlant(Plant plant, Set<Animal> potentialEaters) {
        List<Animal> tempEaters = new ArrayList<>();
        Integer currentMaxEnergy = 0;
        for (Animal potentialEater : potentialEaters) {
            if (potentialEater.getEnergy().equals(currentMaxEnergy)) {
                tempEaters.add(potentialEater);
            } else if (potentialEater.getEnergy() > currentMaxEnergy) {
                tempEaters.clear();
                tempEaters.add(potentialEater);
                currentMaxEnergy = potentialEater.getEnergy();
            }
        }
        tempEaters.forEach(eater -> {
            eater.addEnergy(plantEnergy / tempEaters.size());
            this.allAnimalsEnergy += plantEnergy / tempEaters.size();
        });
        removePlant(plant);
    }

    public void reproduceAnimals() {
        Iterator iterator;
        List<Set<Animal>> animalSets = new ArrayList<>();
        List<Pair<Animal, Animal>> potentialParents = new ArrayList<>();
        Animal tempAnimal1;
        Animal tempAnimal2;
        animals.keySet()
                .forEach(position -> animalSets.add(animals.get(position)));
        for (Set<Animal> set : animalSets) {
            if (set.size() <= 1)
                continue;
            iterator = set.iterator();
            while (iterator.hasNext()) {
                tempAnimal1 = (Animal) iterator.next();
                if (!tempAnimal1.canReproduce())
                    continue;
                if (iterator.hasNext())
                    tempAnimal2 = (Animal) iterator.next();
                else
                    break;
                while (iterator.hasNext() && !tempAnimal2.canReproduce())
                    tempAnimal2 = (Animal) iterator.next();
                if (tempAnimal2.canReproduce()) {
                    potentialParents.add(new Pair<>(tempAnimal1, tempAnimal2));
                }
            }
        }
        potentialParents.forEach(pair -> {
            this.allAnimalsEnergy -= pair.getKey().getEnergy() + pair.getValue().getEnergy();
            pair.getKey().reproduce(pair.getValue());
            this.allAnimalsEnergy += pair.getKey().getEnergy() + pair.getValue().getEnergy();
            this.numberOfAllOffsprings += 2;
        });
    }

    public void removedFromMap(Vector2d position, Animal element) {
        animals.get(position).remove(element);
        this.currentNumberOfAnimals--;
        this.numberOfDeadAnimals++;
        this.numberOfAllOffsprings -= element.getOffsprings().size();
        if (animals.get(position).isEmpty())
            animals.remove(position);
    }

    private void removePlant(Plant plant) {
        plants.remove(plant.getPosition());
    }

    public void spentYear() {
        for (int i = 0; i < this.plantsSpawnRatio; i++)
            this.addPlants();
        this.moveAnimals();
        this.eatPlants();
        this.reproduceAnimals();
    }

    public Animal getAnimal(Vector2d vector2d) {
        return animals.get(vector2d).iterator().next();
    }

    public double getAverageEnergy() {
        if (currentNumberOfAnimals == 0)
            return 0;
        return this.allAnimalsEnergy / this.currentNumberOfAnimals;
    }

    public double getAverageYears() {
        if (numberOfDeadAnimals == 0)
            return 0;
        return this.allAnimalYearsLived / this.numberOfDeadAnimals;
    }

    public double getAverageOffspringsNumber() {
        if (this.currentNumberOfAnimals == 0)
            return 0;
        return this.numberOfAllOffsprings / this.currentNumberOfAnimals;
    }
}
