package world;
import ast.Sensor;
import exceptions.IllegalCoordinateException;
import exceptions.SyntaxError;
import interpret.Outcome;
import static parse.TokenType.*;
import parse.TokenType;

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
    public String name;

    public WorldConstants constants;


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
                        map[i][j] = new Rock(i,j, constants);
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
            initializeConstants(new FileReader("Constants.txt"));
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
            initializeConstants(new FileReader("Constants.txt"));
        }
        catch(FileNotFoundException e) {
            System.out.println("No constants file found.");
            System.exit(1);
        }
        COLUMNS = constants.DEFAULT_COLS;
        ROWS = constants.DEFAULT_ROWS;
        map = new Entity[COLUMNS][ROWS];
        name = new Date(System.currentTimeMillis()).toString();
        critters = new LinkedList<>();
        populate();
    }

    private void initializeConstants(FileReader fileReader) {
        constants = new WorldConstants(fileReader);
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
    
    public void move(Critter c, Coordinate cor) {
    	Coordinate old = c.getLocation();
    	map[cor.getCol()][cor.getRow()] = c;
    	map[old.getCol()][old.getRow()] = null;
    }

    public void evaluate(Outcome outcome) {
        Critter critter = outcome.getCritter();
        TokenType action = outcome.getAction();
        int expr = 0;
        if(action == TAG || action == SERVE){
            expr = outcome.getExpr().evaluate(critter, this);
        }

        switch(action){
            //only the world knows whether the location is valid or not
            //...so if the critter can't move, the world has to manually deduct energy.
            //ack.
            case FORWARD:
            	try{
	            	Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
	            	if(hexAt(forward) == null && inBounds(forward)){
	            		move(critter,forward);
	                    critter.move(forward);
                    }
	                else{
	                    critter.consumeEnergy(critter.size() * constants.MOVE_COST);
                    }
            	}
            	catch(IllegalCoordinateException e){
            		critter.consumeEnergy(critter.size() * constants.MOVE_COST);
                }
            	break;
            case BACKWARD:
            	try{
	                Coordinate backward = Sensor.coordAheadAt(critter, this, -1);
	                if(hexAt(backward) == null && inBounds(backward)){
                        move(critter, backward);
	                    critter.move(backward);

	                }
	                else{
	                    critter.consumeEnergy(critter.size() * constants.MOVE_COST);

	                }
            	}
                catch(IllegalCoordinateException e){
                	critter.consumeEnergy(critter.size() * constants.MOVE_COST);

            	}
            	break;
            case WAIT:
                critter.absorb();
                break;

            case LEFT:
                critter.turn(LEFT);
                break;

            case RIGHT:
                critter.turn(RIGHT);
                break;

            case EAT:
            	try{
	            	Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
	                Food eaten = critter.eat(hexAt(forward));
	                if(eaten != null && eaten.isConsumed()){ //the magic of short-circuiting!
	                    clean(eaten);
	                }

            	}
            	catch(IllegalCoordinateException e){
            		critter.consumeEnergy(critter.size());

            	}
                break;
            case ATTACK:
            	try{
                    Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
	            	Critter attacked = critter.attack(hexAt(forward));

                    judge(attacked);

            	}
            	catch(IllegalCoordinateException e) {
            		critter.consumeEnergy(critter.size()*constants.ATTACK_COST);

            	}
            	break;
            case GROW:
                critter.grow();

                break;
            case BUD:
                try {
                    Coordinate backward = Sensor.coordAheadAt(critter, this, -1);
                    if (inBounds(backward))
                    	add(critter.bud(backward));
                    else
                    	critter.consumeEnergy(critter.size()*constants.BUD_COST);

                }
                catch(IllegalCoordinateException e){
                    critter.consumeEnergy(critter.size() * constants.BUD_COST);

                }
                break;
            case MATE:
                try {
                    Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
                    Critter baby = critter.woo(hexAt(forward), getRandomNearbyUnoccupiedLocation(critter.getLocation()));
                    add(baby);
                    try {
                        judge((Critter) hexAt(forward));

                    } catch (ClassCastException e) {
                        //pls don't mate with rocks

                    }
                }
                catch(IllegalCoordinateException e){

                }
                break;
            case TAG:
            	try{
                    Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
	            	critter.tag(hexAt(forward), expr);

            	}
            	catch(IllegalCoordinateException e) {
            		critter.consumeEnergy(critter.size());

            	}
                break;
            case SERVE:
                try {
                    Coordinate forward = Sensor.coordAheadAt(critter, this, 1);
                    Food dinner = critter.serve(expr, forward);
                    if (dinner != null) {
                        //add will fail if the space is occupied
                        add(dinner);

                    }
                }
                catch(IllegalCoordinateException e){
                    critter.consumeEnergy(critter.size());
                }
                break;

        }
        //kill the critter if it died.
        judge(critter);
    }

    /**
     * Adds an entity to the world.
     * Precondition: e is a valid location and the location to be added onto is empty.
     * Silently fails otherwise.
     * @param e Entity to be added.
     */
    public void add(Entity e) {
        //System.out.println(e.getClass());
        //System.out.println(e.getLocation().getRow());
        if (e != null && hexAt(e.getLocation()) == null) {
            map[e.getCol()][e.getRow()] = e;
            if(e instanceof Critter){ //Forgive me father, for I have sinned
                critters.add((Critter) e);
            }
        }
    }

    public void addRandom(Entity e){
        try{
        	Coordinate c = getRandomUnoccupiedLocation();
            e.location.move(c.getCol(),c.getRow());
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

    //I'm so sorry
    //Partner's code in Sensor is currently not portable (works specifically for critters only because it uses
    //their own direction and returns an int).  I refactored it slightly for ahead so I could use it, but
    //refactoring nearby is just not happening.  Too many calls rely on critter right now and I don't feel like
    //discovering if intelliJ can refactor that automatically.  also, slightly burnt out.  :(

    /**
     * Gets a random unoccupied location next to the given location, or null if all are filled.
     * @param c coordinate to look around from
     * @return random unoccupied location next to c or null if there are none
     */
    public Coordinate getRandomNearbyUnoccupiedLocation(Coordinate c){
        ArrayList<Coordinate> coords = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            Coordinate newCoordinates = null;
            try{
                switch (i){
                    case 0:
                        newCoordinates = new Coordinate(c.getCol(),c.getRow()+1);
                        break;
                    case 1:
                        newCoordinates = new Coordinate(c.getCol()+1,c.getRow()+1);
                        break;
                    case 2:
                        newCoordinates = new Coordinate(c.getCol()+1,c.getRow());
                        break;
                    case 3:
                        newCoordinates = new Coordinate(c.getCol(),c.getRow()-1);
                        break;
                    case 4:
                        newCoordinates = new Coordinate(c.getCol()-1,c.getRow()-1);
                        break;
                    case 5:
                        newCoordinates = new Coordinate(c.getCol()-1,c.getRow());
                        break;
                }

            }
            catch(IllegalCoordinateException e){
                //do nothing and continue looping
            }
            if(inBounds(newCoordinates) && hexAt(newCoordinates) == null){
                coords.add(newCoordinates);
            }
        }
        Collections.shuffle(coords);
        if(!coords.isEmpty()){
            return coords.get(0);
        }
        else{
            return null;
        }


    }


    /**
     * Kills a critter that has run out of energy and removes it from the map and critter list
     * @param c The heretic to be smited
     */
    public void kill(Critter c){
        map[c.getCol()][c.getRow()] = null; //just in case
        map[c.getCol()][c.getRow()] = new Food(c.getCol(), c.getRow(),
                c.size() * constants.FOOD_PER_SIZE, constants);
        critters.remove(c);
    }

    /**
     * Removes a piece of food that has been completely eaten
     * @param f The food object to remove
     */
    public void clean(Food f){
        map[f.getCol()][f.getRow()] = null;
    }

    public boolean judge(Critter c){
        if(c != null){
            if(c.isDead()){
                kill(c);
                return true;
            }
            return false;
        }
        return false;
    }



}
