package EvolutionGame.mapElement.animal;

import EvolutionGame.data.Vector2d;
import EvolutionGame.data.MapDirection;
import EvolutionGame.map.WorldMap;
import EvolutionGame.map.visualisation.MapVisualiser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AnimalTest {
    private Random random = new Random();
    private Vector2d mapBounds = new Vector2d((30 / 2), (30 / 2));
    private Vector2d jungleBounds = new Vector2d((30 / 2), (30 / 2));
    private WorldMap testMap = new WorldMap(mapBounds, jungleBounds, 0, 10, 2, 1, new MapVisualiser(mapBounds, jungleBounds, 0));
    private List<Integer> temp = new ArrayList<>();

    @Test
    public void testToString() {
        Assert.assertEquals(new Animal(testMap, new Vector2d(1, 1), MapDirection.NORTH, temp, 5).toString(), "N");
    }
}
