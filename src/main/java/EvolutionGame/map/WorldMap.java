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
    private final HashMap<List<Integer>, Integer> dominantGenes;
    private List<Integer> currentDominantGenes;
    private Integer currentDominantGenesNumber = 0;
    private final HashMap<List<Integer>, Integer> dominantGenesThroughAllYears;
    private List<Integer> currentDominantGenesThroughAllYears;
    private Integer currentDominantGenesNumberThroughAllYears = 0;
    private int year = 0;

    public WorldMap(Vector2d mapBounds, Vector2d jungleBounds, Integer plantEnergy, Integer animalStartingEnergy, int plantsSpawnRatio, Integer moveEnergy, IElementObserver visualiser) {
        this.moveEnergy = moveEnergy;
        this.mapBounds = mapBounds;
        this.plantEnergy = plantEnergy;
        this.jungleBounds = jungleBounds;
        this.animalStartingEnergy = animalStartingEnergy;
        this.currentDominantGenes = new ArrayList<>();
        this.currentDominantGenesThroughAllYears = new ArrayList<>();
        List<Vector2d> tempList = new ArrayList<>(jungleBounds.opposite().square(jungleBounds));
        Collections.shuffle(tempList);
        this.freeJunglePlants = new LinkedList<>(tempList);
        tempList = new ArrayList<>(prepareFreeSteppesPlants());
        Collections.shuffle(tempList);
        this.freeSteppesPlants = new LinkedList<>(tempList);
        this.plantsSpawnRatio = plantsSpawnRatio;
        this.visualiser = visualiser;
        this.dominantGenes = new HashMap<>();
        this.dominantGenesThroughAllYears = new HashMap<>();
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
        return this.mapBounds.opposite().precedes(position) && this.mapBounds.follows(position);
    }

    private boolean isInJungle(Vector2d position) {
        return position.follows(jungleBounds.opposite()) && position.precedes(jungleBounds);
    }


    private Queue<Vector2d> prepareFreeSteppesPlants() {
        return mapBounds
                .opposite()
                .square(mapBounds)
                .stream()
                .filter(vector2d -> !freeJunglePlants.contains(vector2d))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void addPlants() {
        List<Vector2d> tempList = new ArrayList<>(this.freeJunglePlants);
        Collections.shuffle(tempList);
        if (this.freeJunglePlants.size() != 0)
            this.freeJunglePlants = new LinkedList<>(tempList);
        tempList = new ArrayList<>(this.freeSteppesPlants);
        Collections.shuffle(tempList);
        if (this.freeSteppesPlants.size() != 0)
            this.freeSteppesPlants = new LinkedList<>(tempList);
        for (int i = 0; i < this.plantsSpawnRatio && freeJunglePlants.size() >= 1; i++)
            addPlantsToArea(freeJunglePlants);
        for (int i = 0; i < this.plantsSpawnRatio && freeSteppesPlants.size() >= 1; i++)
            addPlantsToArea(freeSteppesPlants);
    }

    private void addPlantsToArea(Queue<Vector2d> area) {
        Vector2d v = area.poll();
        for (int i = 1; i < area.size() && !(animals.get(v) == null); i++) {
            area.add(v);
            v = area.poll();
        }
        if (!(animals.get(v) == null)) {
            area.add(v);
            return;
        }
        Plant tempPlant = new Plant(v);
        visualiser.addPlant(tempPlant);
        addPlant(tempPlant);
        currentNumberOfPlants++;
    }

    public void moveAnimals() {
        animals.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toList())
                .forEach(animal -> {
                    if (animal.move()) {
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
//        System.out.println("aaaa " + eatenPlants.size());
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
        Integer tempGenesNumber = dominantGenes.get(animal.getGenes());
        Integer tempGenesNumberThroughYears = dominantGenesThroughAllYears.get(animal.getGenes());
        if (tempGenesNumber == null) {
            tempGenesNumber = 1;
            dominantGenes.put(animal.getGenes(), tempGenesNumber);
        } else {
            dominantGenes.remove(animal.getGenes());
            tempGenesNumber++;
            dominantGenes.put(animal.getGenes(), tempGenesNumber);
        }
        if (tempGenesNumber > currentDominantGenesNumber) {
            currentDominantGenesNumber = tempGenesNumber;
            currentDominantGenes = animal.getGenes();
        }
        if (tempGenesNumberThroughYears == null) {
            tempGenesNumberThroughYears = 1;
            dominantGenesThroughAllYears.put(animal.getGenes(), tempGenesNumberThroughYears);
        } else {
            dominantGenesThroughAllYears.remove(animal.getGenes());
            tempGenesNumberThroughYears++;
            dominantGenesThroughAllYears.put(animal.getGenes(), tempGenesNumberThroughYears);
        }
        if (tempGenesNumberThroughYears > currentDominantGenesNumberThroughAllYears) {
            currentDominantGenesNumberThroughAllYears = tempGenesNumberThroughYears;
            currentDominantGenesThroughAllYears = animal.getGenes();
        }
    }

    @Override
    public void addPlant(Plant plant) {
        this.plants.put(plant.getPosition(), plant);
    }

    public void eatPlants() {
        for (Plant plant : eatenPlants) {
            currentNumberOfPlants--;
            if (isInJungle(plant.getPosition()))
                freeJunglePlants.add(plant.getPosition());
            else
                freeSteppesPlants.add(plant.getPosition());
            eatPlant(plant, animals.get(plant.getPosition()));
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
            pair.getKey().reproduce(pair.getValue(), this.year);
            this.allAnimalsEnergy += pair.getKey().getEnergy() + pair.getValue().getEnergy();
            this.numberOfAllOffsprings += 2;
        });
    }

    public void removedFromMap(Vector2d position, Animal element) {
        animals.get(position).remove(element);
        this.currentNumberOfAnimals--;
        this.numberOfDeadAnimals++;
        this.allAnimalYearsLived += element.getYears();
        this.numberOfAllOffsprings -= element.getOffsprings().size();
        if (animals.get(position).isEmpty())
            animals.remove(position);
        Integer tempGenesNumber = dominantGenes.remove(element.getGenes());
        dominantGenes.put(element.getGenes(), tempGenesNumber - 1);
        if (tempGenesNumber.equals(currentDominantGenesNumber))
            currentDominantGenesNumber--;
    }

    private void removePlant(Plant plant) {
        plants.remove(plant.getPosition());
    }

    public void spentYear() {
        this.addPlants();
        this.moveAnimals();
        this.eatPlants();
        this.reproduceAnimals();
        this.year++;
    }

    public List<Integer> getCurrentDominantGenes() {
        return this.currentDominantGenes;
    }

    public Animal getAnimal(Vector2d vector2d) {
        if (animals.get(vector2d) != null) {
            if (animals.get(vector2d).iterator().hasNext())
                return animals.get(vector2d).iterator().next();
        }
        return null;
    }

    public Set<Animal> getAnimalsFromPosition(Vector2d position) {
        return this.animals.get(position);
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

    public List<Integer> getCurrentDominantGenesThroughAllYears() {
        return Collections.unmodifiableList(this.currentDominantGenesThroughAllYears);
    }

    public Vector2d generateFreePositionNear(Vector2d position) {
        List<Vector2d> nearPositions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == position.x && j == position.y)
                    continue;
                nearPositions.add(new Vector2d(i, j));
            }
        }
        Collections.shuffle(nearPositions);
        Queue<Vector2d> queue = new LinkedList<>(nearPositions);
        Vector2d tempVector;
        while (!queue.isEmpty()) {
            tempVector = queue.poll();
            if (this.animals.get(tempVector) == null && this.plants.get(tempVector) == null && checkBounds(tempVector))
                return tempVector;
        }
        return position;
    }
}
