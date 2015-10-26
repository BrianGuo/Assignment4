package world;

import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;

import java.io.Reader;

public class Factory {


    /**
     * //TODO: THIS SHOULD RETURN A RANDOMZIED WORLD
     * Creates a blank world.  Handles String conversions and stuff.
     * @param cols # of cols int he world
     * @param rows # of rows int he world
     * @param name # name of the world
     * @return An empty world
     */
    protected static World getWorld( String cols, String rows, String name) throws SyntaxError{
        return new World(Integer.parseInt(cols), Integer.parseInt(rows), name);
    }

    public static World getRandomWorld() throws SyntaxError{
        return new World();
    }

    /**
     * Creates a world.
     * Called from simulator
     * @param r Reader to read from
     * @return World read from r
     * @throws SyntaxError if the world file has invalid irrecoverable syntax errors
     */
    public static World getWorld(Reader r) throws SyntaxError{
        return World.parseWorld(r);
    }

    public static Critter getCritter(String file) throws IllegalCoordinateException{
        //TODO
        return null;
    }

    public static Critter getCritter(String name, String col, String row, String direction) {
        //TODO
        return null;
    }
    public static Rock getRock(String col, String row) throws IllegalCoordinateException{
        return new Rock(Integer.parseInt(col), Integer.parseInt(row));
    }

    public static Food getFood(String col, String row, String amt) throws IllegalCoordinateException{
        //TODO: Implement FOOD_PER_SIZE
        return new Food(Integer.parseInt(col), Integer.parseInt(row), Integer.parseInt(amt));
    }


}
