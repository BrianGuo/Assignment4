package world;

import exceptions.IllegalCoordinateException;

/**
 * Represents something in the world.
 */
abstract public class Entity {
    private Coordinate location;

    /**
     * What the Entity looks like when printed to the console
     */
    public abstract String toString();

    /**
     * What the Entity looks like when sensed
     */
    public abstract int appearance();

    public Entity(Coordinate location){
        this.location = location;
    }
    public Entity(){
        location = null;
    }
    public Entity(int x, int y) throws IllegalCoordinateException{
        this.location = new Coordinate(x,y);
    }

    public Coordinate getLocation(){
        return location;
    }

    public int getCol(){
        return location.getCol();
    }

    public int getRow(){
        return location.getRow();
    }

    public void move (Coordinate c) throws IllegalCoordinateException{
        location.move(c.getCol(), c.getRow());
    }
}
