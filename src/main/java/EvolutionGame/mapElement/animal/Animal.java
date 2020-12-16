package EvolutionGame.mapElement.animal;

import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IElementObserver;
import EvolutionGame.map.IWorldMap;
import EvolutionGame.data.MapDirection;

import java.util.*;

public class Animal { //TODO dodaj id i sprawdzaj powtorki na globalnej liscie/mapie chodzi o to aby w setach animale nie znikały
    private MapDirection mapDirection;
    private Vector2d position;
    private final IWorldMap map;
    private final List<Integer> genes;
    private List<IElementObserver> observers = new ArrayList<>();
    private final Random geneGenerator = new Random();
    private Integer energy;
    private int years = 0;
    private List<Animal> offsprings = new ArrayList<>();
    private long id;

    public Animal(IWorldMap map, Vector2d position, MapDirection mapDirection, List<Integer> genes, Integer energy) {
        this.map = map;
        this.position = position;
        this.mapDirection = mapDirection;
        this.genes = genes;
        Collections.sort(this.genes);
        this.energy = energy;
        map.place(this);
        this.id = geneGenerator.nextLong();
    }

    public Vector2d getPosition() {
        return this.position;
    }

    private MapDirection getMapDirection() {
        return this.mapDirection;
    }

    private void addOffspring(Animal animal) {
        this.offsprings.add(animal);
    }

    public List<Animal> getOffsprings() {
        return this.offsprings;
    }

    @Override
    public String toString() {
        return String.valueOf(mapDirection.toString().charAt(0));
    }

    private void turn() {
        mapDirection = MapDirection.values()[(mapDirection.ordinal() + chooseDirection()) % MapDirection.values().length];
    }

    private Integer chooseDirection() {
        return genes.get(geneGenerator.nextInt(32));
    }

    public boolean move() {
        if (energy <= 0) {
            removedFromMap();
            return false;
        }
        turn();
        Vector2d oldPosition = this.position;
        this.years++;
        position = checkBounds(position.add(mapDirection.toUnitVector()));
        positionChanged(oldPosition);
        return true;
    }

    public int getYears() {
        return this.years;
    }

    public void subtractEnergy(Integer energy) {
        this.energy -= energy;
    }

    public List<Integer> getGenes() {
        return this.genes;
    }

    public Integer getEnergy() {
        return this.energy;
    }

    private void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public boolean canReproduce() { //TODO jakas globalna wartosc energi pcozatkwoej
        return this.energy > map.getAnimalStartingEnergy() / 2;
    }

    public void reproduce(Animal mate) {
        Animal animal = new Animal(this.map, this.position, this.mapDirection, generateOffspringGenes(mate), generateOffspringEnergy(mate));
        addOffspring(animal);
        mate.addOffspring(animal);
    }

    private long getId() {
        return this.id;
    }

    private Integer generateOffspringEnergy(Animal mate) {
        Integer offspringEnergy = this.energy / 4 + mate.getEnergy() / 4;
        this.energy = this.energy / 4 * 3;
        mate.setEnergy((mate.getEnergy() / 4) * 3);
        return offspringEnergy;
    }

    private List<Integer> generateOffspringGenes(Animal mate) {
        List<Integer> offspringGenes = new ArrayList<>();
        int[] check = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 21; i++) {
            int temp = geneGenerator.nextInt(32);
            offspringGenes.add(this.genes.get(temp));
            check[this.genes.get(temp)]++;
        }
        for (int i = 21; i < 32; i++) {
            int temp = geneGenerator.nextInt(32);
            offspringGenes.add((mate.getGenes().get(temp)));
            check[mate.getGenes().get(temp)]++;
        }
        for (int i = 0; i < 8; i++) {
            if (check[i] == 0) {
                offspringGenes.add(i);
                check[i]++;
            }
        }
        while (offspringGenes.size() > 32) {
            int rand = geneGenerator.nextInt(8);
            if (check[rand] > 1)
                offspringGenes.remove((Integer) rand);
        }
        return offspringGenes;
    }

    public void addEnergy(Integer energy) {
        this.energy += energy;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        Animal o = (Animal) other;
        if (((Animal) other).getId() != this.getId())
            return false;
        if (!((Animal) other).getGenes().equals(this.getGenes()))
            return false;
        if (!((Animal) other).getEnergy().equals(this.getEnergy()))
            return false;
        return this.getPosition().equals(o.getPosition())
                && this.getMapDirection().equals(o.getMapDirection());
    }

    public void addObserver(IElementObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IElementObserver observer) {
        observers.remove(observer);
    }

    public long getNumberOfDescendant(Set<Animal> animals){
        long i = 0;
        for (Animal offspring : offsprings) {
            i += 1;
            if(!animals.contains(offspring)){
                animals.add(offspring);
                i += offspring.getNumberOfDescendant(animals);
            }
        }
        return i;
    }

    private void positionChanged(Vector2d oldPosition) {
        observers.forEach(observer -> observer.positionChanged(this, oldPosition));
    }

    private void removedFromMap() {
        observers.forEach(observer -> observer.removedFromMap(position, this));
    }

    private Vector2d checkBounds(Vector2d position) {
        if (!map.checkBounds(position))
            return position;
        return position.subtract(this.mapDirection.toUnitVector()).opposite();
    }
}
