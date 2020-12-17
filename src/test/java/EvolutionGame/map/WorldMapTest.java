package EvolutionGame.map;

import EvolutionGame.data.MapDirection;
import EvolutionGame.data.Vector2d;
import EvolutionGame.map.visualisation.MapVisualiser;
import EvolutionGame.mapElement.animal.Animal;
import EvolutionGame.mapElement.plant.Plant;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldMapTest {
    private Vector2d mapBounds = new Vector2d((12 / 2), (12 / 2));
    private Vector2d jungleBounds = new Vector2d((6 / 2), (6 / 2));
    private MapVisualiser mapVisualiser = new MapVisualiser(mapBounds, jungleBounds, 0);
    private WorldMap map = new WorldMap(mapBounds, jungleBounds, 9, 10, 1, 1, mapVisualiser);
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
        mapVisualiser.addIWorldMap(map);
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
        mapVisualiser.addIWorldMap(map);
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            genes.add(0);
        }
        map.addPlants();
        Plant tempPlant = map.getPlants().get(map.getPlants().keySet().iterator().next());
        Animal animal = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 10);
        Animal animal1 = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 5);
        Animal animal2 = new Animal(map, tempPlant.getPosition().subtract(new Vector2d(0, 1)), MapDirection.NORTH, genes, 10);
        map.moveAnimals();
        map.eatPlants();
        Assert.assertEquals(13, (int) animal.getEnergy());
        Assert.assertEquals(4, (int) animal1.getEnergy());
        Assert.assertEquals(13, (int) animal2.getEnergy());
    }

    @Test
    public void testReproduceAnimals() {
        mapVisualiser.addIWorldMap(map);
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
        mapVisualiser.addIWorldMap(map);
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
        Vector2d mapBounds = new Vector2d((6 / 2), (6 / 2));
        Vector2d jungleBounds = new Vector2d((0 / 2), (0 / 2));
        WorldMap map = new WorldMap(mapBounds, jungleBounds, 9, 10, 2, 1, mapVisualiser);
        mapVisualiser.addIWorldMap(map);
        Animal animal = new Animal(map, new Vector2d(2, 2), MapDirection.NORTH, genes, 10);
        animal.move();
        animal.move();
        animal.move();
        Assert.assertEquals(new Vector2d(-2, -2), animal.getPosition());
    }

    @Test
    public void testCurrentDominantGenes(){
        mapVisualiser.addIWorldMap(map);
        List<Integer> genes = new ArrayList<>();
        List<Integer> genes2 = new ArrayList<>();
        List<Integer> genes1 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(0);
        for (int i = 0; i < 32; i++)
            genes1.add(1);
        for (int i = 0; i < 32; i++)
            genes2.add(2);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 10);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 10);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes2, 5);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes2, 5);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes2, 5);
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes1, 10);
        Assert.assertEquals(genes2, map.getCurrentDominantGenes());
        map.moveAnimals();
        map.moveAnimals();
        map.moveAnimals();
        map.moveAnimals();
        map.moveAnimals();
        map.moveAnimals();
        map.moveAnimals();
        new Animal(map, new Vector2d(0, 1), MapDirection.NORTH, genes, 5);
        Assert.assertEquals(map.getCurrentNumberOfAnimals(), 4);
        Assert.assertEquals(genes, map.getCurrentDominantGenes());
    }

}
