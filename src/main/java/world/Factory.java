package world;

import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import parse.CritterParser;
import parse.ParserFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Factory {


    /**
     * Creates a blank world.  Handles String conversions and stuff.
     * @param cols # of cols int he world
     * @param rows # of rows int he world
     * @param name # name of the world
     * @return An empty world
     */
    public static World getWorld( String cols, String rows, String name) throws SyntaxError{
        return new World(Integer.parseInt(cols), Integer.parseInt(rows), name);
    }

    /**
     * Creates a random world.
     * @return A new random world with values initialized from constants.
     */
    public static World getRandomWorld() {
        try {
            return new World();
        } catch (SyntaxError e) {
            //this shouldn't happen
            return null;
        }
    }


    /**
     * Creates a world.
     * Called from simulator
     * @param r Reader to read from
     * @return World read from r
     * @throws SyntaxError if the world file has invalid irrecoverable syntax errors
     */
    public static World getWorld(Reader r) throws SyntaxError, FileNotFoundException{
        return ParserFactory.getWorldParser().parseWorld(r);
    }

    /**
     * Reads in  a critter from a file to be placed at a random location with random direction.
     * @param file file specifying the critter
     * @param constants constants file
     * @return A new critter with dummy location and random direction
     * @throws SyntaxError If critter file has errors
     * @throws FileNotFoundException If critter file does not exist
     */
    public static Critter getCritter(String file, WorldConstants constants) throws SyntaxError, FileNotFoundException{
        return CritterParser.parseCritter(new FileReader(file), constants);
    }

    /**
     * Reads in a critter from a file and places it at a specified location with specified direction.
     * @param file file specifying the critter
     * @param col column to place the critter
     * @param row row to place the critter
     * @param direction direction the critter should face
     * @param constants constants file
     * @return A new critter at the given location with given direction
     * @throws SyntaxError if critter file has errors
     * @throws FileNotFoundException if critter file does not exist
     */
    public static Critter getCritter(String file, String col, String row,
                                     String direction, WorldConstants constants) throws SyntaxError, FileNotFoundException {
        Critter critter = getCritter(file, constants);
        critter.location = new Coordinate(Integer.parseInt(col), Integer.parseInt(row));
        critter.setDirection(Integer.parseInt(direction));
        return critter;
    }

    /**
     * Creates a rock on a certain position.
     * @param col Col to place the rock
     * @param row Row to place the rock
     * @param constants constants file
     * @return Rock at the given position
     * @throws IllegalCoordinateException
     */
    public static Rock getRock(String col, String row, WorldConstants constants) throws IllegalCoordinateException{
        return new Rock(Integer.parseInt(col), Integer.parseInt(row), constants);
    }

    /**
     * Creates a food on a certain position.
     * @param col Col to place the food
     * @param row Row to place the food
     * @param amt Raw amount of food to place
     * @param constants constants file
     * @return A new Food object at the given position
     * @throws IllegalCoordinateException
     */
    public static Food getFood(String col, String row, String amt, WorldConstants constants) throws IllegalCoordinateException{
        return new Food(Integer.parseInt(col), Integer.parseInt(row), Integer.parseInt(amt), constants);
    }


}
