package EvolutionGame.data;


import java.util.*;

public class Vector2d {
    public final Integer x;
    public final Integer y;

    public Vector2d(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + this.x.toString() + "," + this.y.toString() + ")";
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (this.x >= other.x && this.y >= other.y);
    }

    Vector2d upperRight(Vector2d other) {
        return new Vector2d((Math.max(this.x, other.x)), (Math.max(this.y, other.y)));
    }

    Vector2d lowerLeft(Vector2d other) {
        return new Vector2d((Math.min(this.x, other.x)), (Math.min(this.y, other.y)));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d((this.x + other.x), (this.y + other.y));
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d((this.x - other.x), (this.y - other.y));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (getClass() != other.getClass())
            return false;
        Vector2d o = (Vector2d) other;
        return (this.x.equals(o.x) && this.y.equals(o.y));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public Vector2d opposite() {
        return new Vector2d((-this.x), (-this.y));
    }

    public Queue<Vector2d> square(Vector2d rightCorner) {
        Queue<Vector2d> list = new LinkedList<>();
        for (int i = this.x; i <= rightCorner.x; i++) {
            for (int j = this.y; j <= rightCorner.y; j++) {
                list.add(new Vector2d(i, j));
            }
        }
        return list;
    }
}
