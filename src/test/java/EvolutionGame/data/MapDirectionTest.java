package EvolutionGame.data;

import org.junit.Assert;
import org.junit.Test;

public class MapDirectionTest {
    private MapDirection mapDirection = MapDirection.SOUTH;

    @Test
    public void testNext() {
        mapDirection = MapDirection.SOUTH;
        Assert.assertEquals(mapDirection.next().next(), MapDirection.WEST);
        mapDirection = MapDirection.WEST;
        Assert.assertEquals(mapDirection.next().next(), MapDirection.NORTH);
        mapDirection = MapDirection.NORTH;
        Assert.assertEquals(mapDirection.next().next(), MapDirection.EAST);
        mapDirection = MapDirection.EAST;
        Assert.assertEquals(mapDirection.next().next(), MapDirection.SOUTH);
    }

    @Test
    public void testPrevious() {
        mapDirection = MapDirection.SOUTH;
        Assert.assertEquals(mapDirection.previous().previous(), MapDirection.EAST);
        mapDirection = MapDirection.EAST;
        Assert.assertEquals(mapDirection.previous().previous(), MapDirection.NORTH);
        mapDirection = MapDirection.NORTH;
        Assert.assertEquals(mapDirection.previous().previous(), MapDirection.WEST);
        mapDirection = MapDirection.WEST;
        Assert.assertEquals(mapDirection.previous().previous(), MapDirection.SOUTH);
    }

    @Test
    public void testPreviousAndNext() {
        mapDirection = MapDirection.SOUTH;
        for (int i = 0; i < 8; i++) {
            Assert.assertEquals(mapDirection, mapDirection.next().previous());
            mapDirection = mapDirection.next();
        }
    }

    @Test
    public void testToStringMap() {
        mapDirection = MapDirection.SOUTH;
        Assert.assertEquals(mapDirection.toString(), "South");
        mapDirection = MapDirection.EAST;
        Assert.assertEquals(mapDirection.toString(), "East");
        mapDirection = MapDirection.NORTH;
        Assert.assertEquals(mapDirection.toString(), "North");
        mapDirection = MapDirection.WEST;
        Assert.assertEquals(mapDirection.toString(), "West");
        Assert.assertEquals(MapDirection.NORTH_EAST.toString(), "North-East");
        Assert.assertEquals(MapDirection.NORTH_WEST.toString(), "North-West");
        Assert.assertEquals(MapDirection.SOUTH_EAST.toString(), "South-East");
        Assert.assertEquals(MapDirection.SOUTH_WEST.toString(), "South-West");
    }

    @Test
    public void testToUnitVector() {
        mapDirection = MapDirection.SOUTH;
        Assert.assertEquals(mapDirection.toUnitVector(), new Vector2d(0, -1));
        Assert.assertEquals(MapDirection.WEST.toUnitVector(), new Vector2d(-1, 0));
        Assert.assertEquals(MapDirection.NORTH.toUnitVector(), new Vector2d(0, 1));
        Assert.assertEquals(MapDirection.EAST.toUnitVector(), new Vector2d(1, 0));
        Assert.assertEquals(MapDirection.NORTH_EAST.toUnitVector(), new Vector2d(1, 1));
        Assert.assertEquals(MapDirection.NORTH_WEST.toUnitVector(), new Vector2d(-1, 1));
        Assert.assertEquals(MapDirection.SOUTH_WEST.toUnitVector(), new Vector2d(-1, -1));
        Assert.assertEquals(MapDirection.SOUTH_EAST.toUnitVector(), new Vector2d(1, -1));
    }

}