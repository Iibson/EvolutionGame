package EvolutionGame.data;

import org.junit.Assert;
import org.junit.Test;

public class Vector2dTest {
    private Vector2d vector2d = new Vector2d(1, 2);

    @Test
    public void testToString() {
        Assert.assertEquals(vector2d.toString(), "(1,2)");
    }

    @Test
    public void testPrecedes() {
        Assert.assertTrue(vector2d.precedes(new Vector2d(2, 2)));
        Assert.assertFalse(vector2d.precedes(new Vector2d(1, 1)));
    }

    @Test
    public void testFollows() {
        Assert.assertFalse(vector2d.follows(new Vector2d(2, 2)));
        Assert.assertTrue(vector2d.follows(new Vector2d(1, 1)));
    }

    @Test
    public void testUpperRight() {
        Assert.assertEquals(vector2d.upperRight(new Vector2d(4, -2)), new Vector2d(4, 2));
    }

    @Test
    public void testLowerLeft() {
        Assert.assertEquals(vector2d.lowerLeft(new Vector2d(4, -2)), new Vector2d(1, -2));
    }

    @Test
    public void testAdd() {
        Assert.assertEquals(vector2d.add(new Vector2d(4, -2)), new Vector2d(5, 0));
    }

    @Test
    public void testSubtract() {
        Assert.assertEquals(vector2d.subtract(new Vector2d(4, -2)), new Vector2d(-3, 4));
    }

    @Test
    public void testOpposite() {
        Assert.assertEquals(vector2d.opposite(), new Vector2d(-1, -2));
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(vector2d, new Vector2d(1, 2));
        //Assert.assertTrue(vector2d.equals(Vector2d.builder().x(1).y(2).build()));
        //powyższe linijki testują to samo
    }

    @Test
    public void testSquare() {
        Assert.assertEquals(121, new Vector2d(-5, -5).square(new Vector2d(5, 5)).size());
        Assert.assertEquals(36, new Vector2d(-2, -2).square(new Vector2d(3, 3)).size());
    }
}
