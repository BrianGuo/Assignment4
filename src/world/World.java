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

    public int hex(Coordinate c){
		return 0;
	}
   
    
    protected World(int columns, int rows, String name){
        //TODO: deal with invalid world sizes
        COLUMNS = columns;
        ROWS = rows;
        map = new Entity[COLUMNS][ROWS];
        this.name = name;
    }

    private World(String columns, String rows, String name){
        this(Integer.parseInt(columns), Integer.parseInt(rows), name);
    }

    //TODO: ADD A METHOD TO SET UP A RANDOM WORLD
    /**
     * Creates a world, given a reader.  Intended for use with a factory method.
     * Ignores any line that does not start with "name", "size", "rock", "food", or "critter",
     * including blank lines and comments.
     * @param r Reader input.
     * @return The created world, as according to the file.
     * @throws SyntaxError if the world file has invalid irrecoverable syntax errors
     * (ex. rock -1 0 or rock asdf)
     */
    protected static World parseWorld(Reader r) throws SyntaxError{
        Scanner sc = new Scanner(r);
        String[] cur;

        cur = sc.nextLine().split("\\s+");
        while(!cur[0].equals("name")){
            cur = sc.nextLine().split("\\s+");
        }
        String name = cur[1];

        cur = sc.nextLine().split("\\s+");
        while(!cur[0].equals("size")){
            cur = sc.nextLine().split("\\s+");
        }
        World world = Factory.getWorld(name, cur[1], cur[2]);

        while(sc.hasNext()){
            cur = sc.nextLine().split("\\s+");
            try {
                switch (cur[0]) {
                    case "rock":
                        world.add(Factory.getRock(cur[1],cur[2]));
                        break;
                    case "food":
                        world.add(Factory.getFood(cur[1], cur[2], cur[3]));
                        break;
                    case "critter":
                        world.add(Factory.getCritter(cur[1], cur[2], cur[3], cur[4]));
                        break;
                    default:
                        //ignore
                        break;
                }
            }
            //IllegalCoordinate is for negative coords, array index for too few args
            //IllegalArg if wrong type is provided or food amount <= 0
            catch(IllegalCoordinateException | ArrayIndexOutOfBoundsException | IllegalArgumentException e){
                //e.getStackTrace();
                throw new SyntaxError("Syntax error in world file");
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
            if(e instanceof Critter){ //Forgive me father, for I have sinned
                critters.add((Critter) e);
            }
        }
    }
    
    public LinkedList<Critter> getCritters(){
    	return critters;
    }

}