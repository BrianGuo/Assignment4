package world;
import critter.*;
import interpret.Outcome;

import java.util.LinkedList;

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

    LinkedList<Critter> critters;

    public int hex(int[] n){
		return 0;
	}

    public World(int columns, int rows){
        COLUMNS = columns;
        ROWS = rows;
        map = new Entity[COLUMNS][ROWS];
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
        return inBounds(c.getX(), c.getY());
    }

    public boolean evaluate(Outcome outcome){
        //TODO Implement this
        return false;
    }

}