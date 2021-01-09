package EvolutionGame.data;


public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;


    @Override
    public String toString() {
        switch (this) {
            case EAST:
                return "East";
            case WEST:
                return "West";
            case NORTH:
                return "North";
            case SOUTH:
                return "South";
            case NORTH_EAST:
                return "North-East";
            case NORTH_WEST:
                return "North-West";
            case SOUTH_EAST:
                return "South-East";
            default:
                return "South-West";
        }
    }

    public MapDirection next() {
        switch (this) {
            case EAST:
                return SOUTH_EAST;
            case WEST:
                return NORTH_WEST;
            case NORTH:
                return NORTH_EAST;
            case SOUTH_EAST:
                return SOUTH;
            case NORTH_WEST:
                return NORTH;
            case NORTH_EAST:
                return EAST;
            case SOUTH:
                return SOUTH_WEST;
            default:
                return WEST;
        }
    }

    public MapDirection previous() {
        switch (this) {
            case EAST:
                return NORTH_EAST;
            case NORTH_EAST:
                return NORTH;
            case WEST:
                return SOUTH_WEST;
            case NORTH:
                return NORTH_WEST;
            case NORTH_WEST:
                return WEST;
            case SOUTH_WEST:
                return SOUTH;
            case SOUTH_EAST:
                return EAST;
            default:
                return SOUTH_EAST;
        }
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case SOUTH_EAST:
                return new Vector2d(1, -1); // nowy wektor co wywołanie
            case SOUTH_WEST:
                return new Vector2d(-1, -1);
            case NORTH_EAST:
                return new Vector2d(1, 1);
            case NORTH_WEST:
                return new Vector2d(-1, 1);
            case EAST:
                return new Vector2d(1, 0);
            case WEST:
                return new Vector2d(-1, 0);
            case NORTH:
                return new Vector2d(0, 1);
            default:
                return new Vector2d(0, -1);
        }
    }
}
