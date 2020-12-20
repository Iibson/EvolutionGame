package EvolutionGame.mapElement.animal;

import EvolutionGame.data.Vector2d;
import EvolutionGame.data.MapDirection;
import EvolutionGame.map.WorldMap;
import EvolutionGame.map.visualisation.MapVisualiser;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;


public class AnimalTest {
    private Vector2d mapBounds = new Vector2d((30 / 2), (30 / 2));
    private Vector2d jungleBounds = new Vector2d((30 / 2), (30 / 2));
    private WorldMap testMap = new WorldMap(mapBounds, jungleBounds, 0, 10, 2, 1, new MapVisualiser(mapBounds, jungleBounds, 0));
    private List<Integer> temp = new ArrayList<>();

    @Test
    public void testToString() {
        Assert.assertEquals(new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, temp, 5, 0).toString(), "N");
    }

    @Test
    public void testGeneratingOffspringGenes() {
        List<Integer> genes = new ArrayList<>();
        List<Integer> genes1 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(0);
        for (int i = 0; i < 32; i++)
            genes1.add(1);
        Animal a1 = new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes, 10, 0);
        Animal a2 = new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes1, 10, 0);
        testMap.reproduceAnimals();
        Assert.assertEquals(2, testMap.getAnimalsFromPosition(new Vector2d(1, 1)).size());
        Assert.assertEquals(3, testMap.getCurrentNumberOfAnimals());
        Iterator iterator = testMap.getAnimalsFromPosition(new Vector2d(1, 1)).iterator();
        Animal temp;
        while (iterator.hasNext()) {
            temp = (Animal) iterator.next();
            if(temp.equals(a1) || temp.equals(a2))
                continue;
            Assert.assertTrue(temp.getGenes().contains(0));
            Assert.assertTrue(temp.getGenes().contains(1));
            Assert.assertTrue(temp.getGenes().contains(2));
            Assert.assertTrue(temp.getGenes().contains(3));
            Assert.assertTrue(temp.getGenes().contains(4));
            Assert.assertTrue(temp.getGenes().contains(5));
            Assert.assertTrue(temp.getGenes().contains(6));
            Assert.assertTrue(temp.getGenes().contains(7));
        }
    }

    @Test
    public void testNumberOfDescendants() {
        List<Integer> genes = new ArrayList<>();
        List<Integer> genes1 = new ArrayList<>();
        for (int i = 0; i < 32; i++)
            genes.add(0);
        for (int i = 0; i < 32; i++)
            genes1.add(1);
        Animal testAnimal = new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes, 1000, 0);
        new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes1, 1000, 0);
        new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes1, 1000, 0);
        new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, genes1, 1000, 0);
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                if(i == 1 && j == 1)
                    continue;
                new Animal(testMap, new Vector2d(i, j), MapDirection.NORTH, genes1, 1000, 0);
            }
        }
        testMap.reproduceAnimals();
        testMap.reproduceAnimals();
        Assert.assertEquals(3, testAnimal.getNumberOfDescendantAfterNYears(new HashSet<>(), 10, 0));
    }
}
