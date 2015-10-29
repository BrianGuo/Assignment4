package world;

import exceptions.IllegalCoordinateException;

/**
 * Represents something in the world.
 */
abstract public class Entity {
    protected Coordinate location;
    protected final WorldConstants constants;

    /**
     * What the Entity looks like when printed to the console
     */
    public abstract String toString();

    /**
     * What the Entity looks like when sensed
     */
    public abstract int appearance();
    public Entity(){
        //throw new UnsupportedOperationException();
        constants = null;
    }
    public Entity(Coordinate location, WorldConstants constants){
        this.location = location;
        this.constants = constants;
    }
    public Entity(WorldConstants constants){
        location = null;
        this.constants = constants;
    }
    public Entity(int x, int y, WorldConstants constants) throws IllegalCoordinateException{
        this.location = new Coordinate(x,y);
        this.constants = constants;
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
