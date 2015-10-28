package world;
import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import interpret.Outcome;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }

    static final double ROCK_FREQUENCY = 0.15;
    private final int ROWS;
    private final int COLUMNS;
    private String name;
    public int BASE_DAMAGE, ENERGY_PER_SIZE, FOOD_PER_SIZE, MAX_SMELL_DISTANCE, ROCK_VALUE,
            DEFAULT_COLS, DEFAULT_ROWS, MAX_RULES_PER_TURN, SOLAR_FLUX, MOVE_COST, ATTACK_COST,
            GROW_COST, BUD_COST, MATE_COST, RULE_COST, ABILITY_COST, INITIAL_ENERGY, MIN_MEMORY;
    public double DAMAGE_INC;


    LinkedList<Critter> critters;

    /**
     * Front-end using coordinate.
     * @param c coordinate to look at
     * @return Entity at that location, or null if empty
     */
    public Entity hexAt(Coordinate c) {
        return hexAt(c.getCol(), c.getRow());
    }

    /**
     * Requires: c, r are valid
     * @param c column to look at
     * @param r row to look at
     * @return Entity at that location, or null if empty
     */
    public Entity hexAt(int c, int r){
        if (inBounds(c,r))
            return map[c][r];
        else{
            return null;
        }
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
        try {
            initializeConstants(new FileReader("constants.txt"));
        }
        catch(FileNotFoundException e) {
            System.out.println("No constants file found.");
            System.exit(1);
        }
        COLUMNS = columns;
        ROWS = rows;
        map = new Entity[COLUMNS][ROWS];
        this.name = name;
        critters = new LinkedList<>();
    }

    /**
     * Creates a new world, populated by rocks with frequency ROCK_FREQUENCY
     * Java didn't let me call the constructor with parameters -.-
     */
    public World() throws SyntaxError{
        try {
            initializeConstants(new FileReader("constants.txt"));
        }
        catch(FileNotFoundException e) {
            System.out.println("No constants file found.");
            System.exit(1);
        }
        COLUMNS = DEFAULT_COLS;
        ROWS = DEFAULT_ROWS;
        map = new Entity[COLUMNS][ROWS];
        name = new Date(System.currentTimeMillis()).toString();
        critters = new LinkedList<>();
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
            cur = sc.nextLine().split("\\s+", 1);
        }
        String name = cur[1];

        cur = sc.nextLine().split("\\s+");
        while(!cur[0].equals("size")){
            cur = sc.nextLine().split("\\s+");
        }
        World world = Factory.getWorld(cur[1], cur[2], name);

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
                    //case "critter":
                    //world.add(Factory.getCritter(cur[1], cur[2], cur[3], cur[4]));
                    // break;
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
        return !(c>= COLUMNS || c < 0 || 2*r -c < 0 || 2*r - c >= 2*ROWS-COLUMNS);
    }

    public boolean inBounds(Coordinate c){
        return inBounds(c.getCol(), c.getRow());
    }

    public boolean evaluate(Outcome outcome) {
        //TODO Implement this
        return false;
    }

    /**
     * Adds an entity to the world.
     * Precondition: e is a valid location and the location to be added onto is empty.
     * Silently fails otherwise.
     * @param e Entity to be added.
     */
    public void add(Entity e) {
        //System.out.println(e.getLocation().getRow());
        if (hexAt(e.getLocation()) == null) {
            map[e.getCol()][e.getRow()] = e;
            if(e instanceof Critter){ //Forgive me father, for I have sinned
                critters.add((Critter) e);
            }
        }
    }

    public void addRandom(Entity e){
        try{
            e.move(getRandomUnoccupiedLocation());
            add(e);
        }
        catch(IllegalCoordinateException ex){
            //this shouldn't happen
        }
    }

    public LinkedList<Critter> getCritters(){
        return critters;
    }

    /**
     * Finds a random, unoccupied location in the world
     * @return a random, unoccupied Coordinate
     */
    public Coordinate getRandomUnoccupiedLocation() throws IllegalCoordinateException{
        ArrayList<Coordinate> coords = new ArrayList<>();
        for(int i = 0; i < COLUMNS; i++){
            for(int j = 0; j < ROWS; j++){
                if(map[i][j] == null && inBounds(i,j)){
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




    public void initializeConstants(Reader r){
        Scanner sc = new Scanner(r);


        //at least there's only one double value...
        //didn't want to have to get constants out of a map.
        //java doesn't seem to support the level of reflection required to avoid this switch case block
        while(sc.hasNext()) {
            int value = 0;
            String[] next = sc.nextLine().split("\\s+");
            try{
                value = Integer.parseInt(next[1]);
            }
            catch(NumberFormatException e){
                //handled later
            }
            switch (next[0]) {
                case "BASE_DAMAGE":
                    BASE_DAMAGE = value;
                    break;
                case "ENERGY_PER_SIZE":
                    ENERGY_PER_SIZE = value;
                    break;
                case "FOOD_PER_SIZE":
                    FOOD_PER_SIZE = value;
                    break;
                case "MAX_SMELL_DISTANCE":
                    MAX_SMELL_DISTANCE = value;
                    break;
                case "ROCK_VALUE":
                    ROCK_VALUE = value;
                    break;
                case "COLUMNS":
                    DEFAULT_COLS = value;
                    break;
                case "ROWS":
                    DEFAULT_ROWS = value;
                    break;
                case "MAX_RULES_PER_TURN":
                    MAX_RULES_PER_TURN = value;
                    break;
                case "SOLAR_FLUX":
                    SOLAR_FLUX = value;
                    break;
                case "MOVE_COST":
                    MOVE_COST = value;
                    break;
                case "ATTACK_COST":
                    ATTACK_COST = value;
                    break;
                case "GROW_COST":
                    GROW_COST = value;
                    break;
                case "BUD_COST":
                    BUD_COST = value;
                    break;
                case "MATE_COST":
                    MATE_COST = value;
                    break;
                case "RULE_COST":
                    RULE_COST = value;
                    break;
                case "ABILITY_COST":
                    ABILITY_COST = value;
                    break;
                case "INITIAL_ENERGY":
                    INITIAL_ENERGY = value;
                    break;
                case "MIN_MEMORY":
                    MIN_MEMORY = value;
                    break;
                case "DAMAGE_INC":
                    DAMAGE_INC = Double.parseDouble(next[1]);
                    break;


            }
        }
    }
}
