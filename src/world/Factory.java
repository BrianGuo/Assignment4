package world;

import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import parse.CritterParser;
import parse.ParserFactory;
import parse.WorldParser;

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

    public static Critter getCritter(String file, WorldConstants constants) throws SyntaxError, FileNotFoundException{
        return CritterParser.parseCritter(new FileReader(file), constants);
    }

    public static Critter getCritter(String file, String col, String row,
                                     String direction, WorldConstants constants) throws SyntaxError, FileNotFoundException {
        Critter critter = getCritter(file, constants);
        critter.coordinates = new Coordinate(Integer.parseInt(col), Integer.parseInt(row));
        critter.setDirection(Integer.parseInt(direction));
        return critter;
    }
    public static Rock getRock(String col, String row, WorldConstants constants) throws IllegalCoordinateException{
        return new Rock(Integer.parseInt(col), Integer.parseInt(row), constants);
    }

    public static Food getFood(String col, String row, String amt, WorldConstants constants) throws IllegalCoordinateException{
        return new Food(Integer.parseInt(col), Integer.parseInt(row), Integer.parseInt(amt), constants);
    }


}
