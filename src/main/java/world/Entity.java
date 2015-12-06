package world;

import exceptions.IllegalCoordinateException;

import java.util.ArrayList;

/**
 * Represents something in the world.
 */
abstract public class Entity {
    protected transient Coordinate location;
    protected transient final WorldConstants constants;

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
    public void setLocation(Coordinate c) {
    	location = c;
    }
    public boolean equals(Entity other){
        if(other == null){
            return false;
        }
        return this.hashCode() == other.hashCode();
    }

    public abstract ArrayList<String> properties();
}
