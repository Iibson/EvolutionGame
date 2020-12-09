package EvolutionGame.map;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.mapElement.IMapElement;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldMapTest {

    private WorldMap map = new WorldMap(12, 12, 10, 10, 9, 10, 1);
    private Random random = new Random();

    @Test
    public void testAddPlants() {
        map.addPlants();
        map.addPlants();
        map.addPlants();
        int numberPlantsInJungle = 0;
        int numberPlantsInSteppe = 0;
        for (Vector2d vector2d : map.getPlants().keySet()) {
            if (vector2d.precedes(map.getJungleBounds()) && vector2d.follows(map.getJungleBounds().opposite()))
                numberPlantsInJungle++;
            else
                numberPlantsInSteppe++;
        }
        Assert.assertEquals(numberPlantsInJungle, numberPlantsInSteppe);
    }

    @Test
    public void testAnimalMovementAndAnimalRemoval() {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            genes.add(random.nextInt(8));
        }
        Animal animal = new Animal(map, new Vector2d(1, 1), MapDirection.NORTH, genes, 10);
        animal.move();
        animal.move();
        animal.move();
        Assert.assertEquals(animal.getPosition(), map.getAnimals().get(animal.getPosition()).iterator().next().getPosition());
        map.removedFromMap(animal.getPosition(), animal);
        Assert.assertNull(map.getAnimals().get(animal.getPosition()));
    }

    @Test
    public void testPlantRemoval() {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            genes.add(0);
        }
        map.addPlants();
        Plant tempPlant = map.getPlants().get(map.getPlants().keySet().iterator().next());
        Animal animal = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 10);
        Animal animal1 = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 5);
        Animal animal2 = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 10);
        animal.move();
        animal1.move();
        animal2.move();
        map.eatPlants();
        Assert.assertEquals(13, (int) animal.getEnergy());
        Assert.assertEquals(4, (int) animal1.getEnergy());
        Assert.assertEquals(13, (int) animal2.getEnergy());
    }

    @Test
    public void testReproduceAnimals() {
        List<Integer> genes = new ArrayList<>();
        List<Integer> genes2 = new ArrayList<>();
        List<Integer> genes1 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(0);
        for (int i = 0; i < 32; i++)
            genes1.add(1);
        for (int i = 0; i < 32; i++)
            genes2.add(2);
        Animal animal = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 10);
        Animal animal1 = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes2, 5);
        Animal animal2 = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes1, 10);
        map.reproduceAnimals();
        Assert.assertEquals(map.getAnimals().get(new Vector2d(0, 1)).size(), 4);
        map.getAnimals().get(new Vector2d(0, 1)).remove(animal);
        map.getAnimals().get(new Vector2d(0, 1)).remove(animal1);
        map.getAnimals().get(new Vector2d(0, 1)).remove(animal2);
        Animal animal3 = map.getAnimals().get(new Vector2d(0, 1)).iterator().next();
        Assert.assertEquals(animal3.getGenes().size(), 32);
        for (int i = 0; i < 8; i++) {
            Assert.assertTrue(animal3.getGenes().contains(i));
        }
    }

    @Test
    public void testMoveAnimals() {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(random.nextInt(8));
        Animal animal = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 10);
        genes.clear();
        for (int i = 0; i < 32; i++)
            genes.add(random.nextInt(8));
        Animal animal1 = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 5);
        genes.clear();
        for (int i = 0; i < 32; i++)
            genes.add(random.nextInt(8));
        Animal animal2 = new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 10);
        map.moveAnimals();
        Assert.assertNotEquals(animal.getPosition(), new Vector2d(0, 1));
        Assert.assertNotEquals(animal1.getPosition(), new Vector2d(0, 1));
        Assert.assertNotEquals(animal2.getPosition(), new Vector2d(0, 1));
    }

    @Test
    public void testMapBounds() {
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(0);
        WorldMap map = new WorldMap(6, 6, 0, 0, 9, 10, 2);
        Animal animal = new Animal(map, new Vector2d(2, 2), MapDirection.NORTH, genes, 10);
        animal.move();
        animal.move();
        animal.move();
        Assert.assertEquals(new Vector2d(-2, -2), animal.getPosition());
    }

}
