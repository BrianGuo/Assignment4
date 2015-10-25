package world;
import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import interpret.Outcome;

import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A representation of the world.
 * Class invariant: critters contains references to all critters in the world
 */
public class World{
    /*
     * Entities are accessed via map[column][row]
     */
    private Entity[][] map;
    private final int COLUMNS;
    private final int ROWS;
    private String name;

    LinkedList<Critter> critters;

    public int hex(int[] n){
		return 0;
	}

    protected World(int columns, int rows, String name){
        COLUMNS = columns;
        ROWS = rows;
        map = new Entity[COLUMNS][ROWS];
        this.name = name;
        //TODO: add randomness
    }

    /**
     * Creates a world, given a reader.  Intended for use with a factory method.
     * @param r Reader input.
     * @return The created world, as according to the file.
     * @throws SyntaxError if the world file has invalid irrecoverable syntax errors
     * (ex. rock -1 0 or rock asdf)
     */
    public World parseWorld(Reader r) throws SyntaxError{
        Scanner sc = new Scanner(r);
        String[] cur;
        cur = sc.nextLine().split("\\s+");
        String name = cur[1];
        cur = sc.nextLine().split("\\s+");
        World world = Factory.getWorld(name, cur[1], cur[2]);

        while(sc.hasNext()){
            cur = sc.nextLine().split("\\s+");
            try {
                switch (cur[0]) {
                    case "rock":
                        add(Factory.getRock(cur[1],cur[2]));
                        break;
                    case "food":
                        add(Factory.getFood(cur[1], cur[2], cur[3]));
                        break;
                    case "critter":
                        add(Factory.getCritter(cur[1], cur[2], cur[3], cur[4]));
                        break;
                    default:
                        throw new SyntaxError();
                }
            }
            //IllegalCoordinate is for negative coords, array index for too few args
            //IllegalArg if wrong type is provided or food amount <= 0
            catch(IllegalCoordinateException | ArrayIndexOutOfBoundsException | IllegalArgumentException e){
                throw new SyntaxError();
            }

        }
        //TODO: Complete the rest...?
        return world;

    }

    /**
     * Checks if a given coordinate is off the edge of the world
     * @param c Column of the hex
     * @param r Row of the hex
     * @return False if c >= COLUMNS, c < 0, 2r-c < 0, or 2r-c >= 2*ROWS-COLUMNS
     * True otherwise.
     */
    public boolean inBounds(int c, int r){
        return !(c>= COLUMNS || c < 0 || 2*r -c > 0 || 2*r - c >= 2*ROWS-COLUMNS);
    }

    public boolean inBounds(Coordinate c){
        return inBounds(c.getCol(), c.getRow());
    }

    public boolean evaluate(Outcome outcome) {
        //TODO Implement this
        return false;
    }

    public void add(Entity e) {
        if (inBounds(e.getLocation())) {
            map[e.getRow()][e.getCol()] = e;
            if(e instanceof Critter){ //I tried to keep this land free of downcasting, but alas, I have failed.  Forgive me
                critters.add((Critter) e);
            }
        }
    }

}