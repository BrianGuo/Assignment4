package world;
import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import interpret.Outcome;

import java.io.Reader;
import java.util.*;

/**
 * A representation of the world.
 * Class invariant: critters contains references to all critters in the world
 * Each element in map either contains a Critter, Food, or Rock object, or null if empty.
 * In hindsight, using the observer pattern would make removing things from the world much easier...
 */
public class World{
    /*
     * Entities are accessed via map[column][row]
     */
    private Entity[][] map;
    private final int COLUMNS;
    private final int ROWS;
    static final int DEFAULT_COLS = 25;
    static final int DEFAULT_ROWS = (int) (DEFAULT_COLS * 1.37);
    static final double ROCK_FREQUENCY = 0.15;
    private String name;

    LinkedList<Critter> critters;

    /**
     * Front-end using coordinate.
     * @param c coordinate to look at
     * @return Entity at that location, or null if empty
     */
    public Entity hexAt(Coordinate c){
        return hexAt(c.getCol(), c.getRow());
    }

    /**
     * Requires: c, r are valid
     * @param c column to look at
     * @param r row to look at
     * @return Entity at that location, or null if empty
     */
    public Entity hexAt(int c, int r){
        return map[c][r];
    }

    /**
     * Prints out a visual representation of what is on the given hex
     * @param col column of the desired entity
     * @param row row of the desired entity
     * @return string representation of the entity (or "-" if empty)
     */
    public String hexToString(int col, int row){
        try{
            return map[col][row].toString();
        }
        catch(NullPointerException e){
            return "-";
        }
    }

    /**
     * Front-end for hexToString() that takes a coordinate
     * @param c
     * @return string representation of the entity whose location is at c (or "-" if empty)
     */
    public String hexToString(Coordinate c){
        return hexToString(c.getCol(), c.getRow());
    }

    /**
     * Populates an empty world with rocks, with a ROCK_FREQUENCY chance per hex.
     * Requires: World is empty.  Otherwise it can overwrite a critter, which is really bad.
     */
    private void populate(){
        for(int i = 0; i < COLUMNS; i++){
            for(int j = 0; j < ROWS; j++){
                if(Math.random() < ROCK_FREQUENCY){
                    try{
                        map[i][j] = new Rock(i,j);
                    }
                    catch(IllegalCoordinateException e){
                        //This will never happen
                    }
                }
            }
        }
    }

    /**
     * The standard constructor for a world.
     * @param columns width of the world in columns
     * @param rows height of the world in rows
     * @param name name of the world
     */
    World(int columns, int rows, String name) throws SyntaxError{
        //invalid world dimensions
        if (2*rows - columns < 0){
        	throw new SyntaxError();
        }
        COLUMNS = columns;
        ROWS = rows;
        map = new Entity[COLUMNS][ROWS];
        this.name = name;
    }

    /**
     * Creates a new world, populated by rocks with frequency ROCK_FREQUENCY
     */
    public World() throws SyntaxError{
        this(DEFAULT_COLS, DEFAULT_ROWS, new Date(System.currentTimeMillis()).toString());
        populate();
    }

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

    private void add(Entity e) {
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

    /**
     * Finds a random, unoccupied location in the world
     * @return a random, unoccupied Coordinate
     */
    private Coordinate getRandomUnoccupiedLocation() throws IllegalCoordinateException{
        ArrayList<Coordinate> coords = new ArrayList<>();
        for(int i = 0; i < COLUMNS; i++){
            for(int j = 0; j < ROWS; j++){
                if(map[i][j] == null){
                        coords.add(new Coordinate(i, j));
                }
            }
        }
        //no unoccupied locations!
        if(coords.isEmpty()) {
            throw new IllegalCoordinateException("No legal spots remaining in the world");
        }
        Collections.shuffle(coords);
        return coords.get(0);
    }

    /**
     * Kills a critter that has run out of energy and removes it from the map and critter list
     * @param c The heretic to be smited
     */
    public void kill(Critter c){
        //TODO Turn critter into food
        map[c.getCol()][c.getRow()] = null;
        critters.remove(c);
    }

    /**
     * Removes a piece of food that has been completely eaten
     * @param f The food object to remove
     */
    public void clean(Food f){
        map[f.getCol()][f.getRow()] = null;
    }
}