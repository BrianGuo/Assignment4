package simulator;



import world.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.*;
import interpret.CritterInterpreter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



public class Simulator {
	
	Interpreter interpreter;
	public World world;
	int current_version_number;
	Entity[][] old;
	ArrayList<ArrayList<Integer>> critterDeaths = new ArrayList<>();
	HashMap<Integer, Critter> oldCritters = new HashMap<>();
	ArrayList<ArrayList<Coordinate>> changes;
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();
	private int timesteps;


	/**
	 * Regular Constructor Used entirely for testing purposes
	 * This shouldn't be used
	 */
	public Simulator() {}
	
	/**
	 * Standard simulator constructor, stores a world and
	 * an interpreter
	 * @param w  the world to be stored
	 * @param i  the interpreter to be stored
	 */
	public Simulator (World w, Interpreter i) {
		interpreter = i;
		world = w;
		if (interpreter != null)
			((CritterInterpreter)interpreter).setWorld(world);
		current_version_number = 0;
		old = new Entity[getWorldColumns()][getWorldRows()];
		oldCritters = new HashMap<>();
		changes = new ArrayList<>();
	}
	
	/**
	 * Stores an interpreter, used when no world yet provided
	 * @param i    the interpreter to store 
	 */
	public Simulator (Interpreter i) {
		interpreter = i;
		current_version_number = 0;
		changes = new ArrayList<>();
	}

	/**
	 * advances the world a number of current_version_number
	 * @param n   the number of current_version_number to advance the world
	 */
	public void advance(int n){
		writeLock.lock();
		try {
			if (hasWorld()) {
				for (int i = 0; i < n; i++) {
					//LinkedList<Critter> copy = new LinkedList<>();
					LinkedHashMap<Integer, Critter> copy = new LinkedHashMap<>(world.getCritters());
					//copy.addAll(world.getCritters());
					for (Critter c : copy.values()) {
						if (!(world.getCritters().containsValue(c)))
							continue;
						((CritterInterpreter) interpreter).setCritter(c);
						Outcome o = interpreter.interpret(c.getProgram());
						world.evaluate(o);
						world.judge(c);
					}
					timesteps++;
					update();
				}
			} else {
				System.out.println("You haven't loaded a world");
			}
		}
		finally{
			writeLock.unlock();
		}

	}
	
	/**
	 * Sets the world field to the parameter
	 * @param w    the world to be set to
	 */
	public void setWorld(World w){
		//TODO: does this need a lock?
		writeLock.lock();
		try {

			world = w;
			if (interpreter != null)
				((CritterInterpreter) interpreter).setWorld(w);
			update();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * Sets the interpreter field to the given interpreter
	 * @param i  the interpreter to set the field to
	 */
	public void setInterpreter(Interpreter i) {
		interpreter = i;
		if (world != null)
			((CritterInterpreter)interpreter).setWorld(world);
	}
	
	/**
	 * Parses a world and sets the world parameter to the 
	 * returned world
	 * @param filename   the filename of the world to be parsed
	 */
	public void parseWorld(String filename){
		try {
			FileReader f = new FileReader(filename);
			parseWorld(f);
		}

		catch (FileNotFoundException e) {
			System.out.println("Your File Was Not Found");
		}

		
	}

	public void parseWorld(Reader f) throws FileNotFoundException{
		this.world = Factory.getWorld(f);
		writeLock.lock(); //should work because Reentrant lock
		try {

			if (interpreter != null)
				((CritterInterpreter) interpreter).setWorld(world);
			changes.add(diffWorld()); //has a readlock
			critterDeaths.add(diffObituaries());
			//old = new Entity[getWorldColumns()][getWorldRows()]; //if the size of the world has changed, any empty node changes will NOT be included in the diff.
			//oldCritters = world.getCritters();
		}
		finally {
			writeLock.unlock();
		}
	}

	/**
	 * Parses a world that is given as a long string of the entire definition.
	 * @param s String that is the world definition
	 */
	public void parseWorldString(String s) throws FileNotFoundException{
		Reader sr = new StringReader(s);
		parseWorld(sr);
	}

	/**
	 * Returns the current_version_number field
	 * @return   the number of current_version_number taken
	 */
	public int getCurrent_version_number() {
		readLock.lock();
		try {
			return current_version_number;
		}
		finally{
			readLock.unlock();
		}
	}
	
	/**
	 * Returns the number of critters in the world
	 * @return    the number of critters in the world
	 */
	public int getNumCritters() {
		readLock.lock();
		try {
			return world.getCritters().size();
		}
		finally{
			readLock.unlock();
		}
	}
	
	/**
	 * Returns true if there is an assigned world
	 * false if null
	 * @return    whether the world field is null
	 */
	public boolean hasWorld() {
		return (world != null);
	}

	/**
	 * Gets the entity at the specified location in the world.
	 * @param col Column to look at
	 * @param row Row to look at
	 * @return The entity at [col, row], or null if empty/OOB
	 */
	public Entity getEntityAt(int col, int row){
		//TODO: does this need a lock?  finalize API
		readLock.lock();
		try {
			return world.hexAt(col, row);
		}
		finally{
			readLock.unlock();
		}
	}

	/**
	 * Returns the number of columns in the underlying world, or 0 if a world has not been loaded yet.
	 * @return # of cols in the world, or 0 if world == null
	 */
	public int getWorldColumns(){
		writeLock.lock();
		try {

			if (hasWorld())
				return world.getColumns();
			return 0;
		}
		finally {
			writeLock.unlock();
		}
	}

	/**
	 * Returns the number of rows in the underlying world, or 0 if a world has not been loaded yet.
	 * @return # of rows in the world, or 0 if world == null
	 */
	public int getWorldRows(){
		writeLock.lock();
		try {

			if (hasWorld())
				return world.getRows();
			return 0;
		}
		finally {
			writeLock.unlock();
		}
	}


	/**
	 * Puts n instances of the critter in the world at random locations
	 * @param filename    the critter to be put
	 * @param n           the number of times to put the critter in the world
	 * @throws MissingElementException     If there is no world loaded
	 * @throws FileNotFoundException	   If the file is not found
	 * @throws SyntaxError				   If the file has a SyntaxError
	 */
	public void putCritterRandomly(String filename, int n) throws MissingElementException, FileNotFoundException, SyntaxError{
		writeLock.lock();
		try {
			if (!hasWorld())
				throw new MissingElementException();
			for (int i = 0; i < n; i++) {
				Critter c = Factory.getCritter(filename, world.constants);
				world.addRandom(c);
			}

		}
		finally{
			writeLock.unlock();
		}
	}

	/**
	 * Adds an entity to the world at a random location
	 * Had to create a new function since putCritterRandomly...only works on critters...and with strings.
	 * @param entity Entity to be added
	 */
	public void addRandomEntity(Entity entity){
		writeLock.lock();
		try {
			world.addRandom(entity);
		}
		finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Helper function that produces one line in ascii hex notation
	 * @param c		Column of starting index
	 * @param r		Row of starting index
	 * @return		A string representation of the line
	 */
	public String hexLine(int c, int r) {
		String result;
		if (c == 1)
			result = "  ";
		else
			result = "";
		while (world.inBounds(c,r)){
			Entity entity = world.hexAt(c,r);
			if (entity == null)
				result += "-";
			else
				result += entity.toString();
			result += "   ";
			c += 2;
			r += 1;
		}
		return result;
	}
	
	/**
	 * Prints information about the contents of a hex coordinate.
	 * @param c		The column of the location to be hexed
	 * @param r		The row of the location to be hexed
	 */
	public ArrayList<String> hexLocation(int c, int r) {
		Entity e = world.hexAt(c, r);
		ArrayList<String> result = new ArrayList<String>();
		if (!world.inBounds(c, r))
			result.add("This coordinate is out of bounds");
		else if (e == null)
			result.add("There is nothing at this coordinate");
		else {
			return e.properties();
		}
		return result;
	}

	/**
	 * Returns entity at coordinate in the world.  Wrapper for world method.
	 */
	public Entity getEntityAt(Coordinate coordinate){
		readLock.lock();
		try{
			return world.hexAt(coordinate);
		}
		finally{
			readLock.unlock();
		}

	}

	/**
	 * Adds an entity to the world
	 * @param entity Entity to be added, with the correct coordinate
	 */
	public void addEntity(Entity entity){
		writeLock.lock();
		try{
			world.add(entity);
			update();
		}
		finally {
			writeLock.unlock();
		}
	}

	/**
	 * Updates the diffs of the world and the timestep.
	 */
	private void update() {
		changes.add(diffWorld());
		critterDeaths.add(diffObituaries());
		current_version_number++;
	}

	/**
	 * Prints the world in ascii hex format.
	 * Requires: world is not null and valid.
	 */
	public void hexWorld() {
		ArrayList<String> results = new ArrayList<String>();
		int columns = world.getColumns();
		int c;
		int r = 0;
		if (columns %2 == 1) {
			c = 1;
		}
		else{
			c = 0;
		}
		while(world.inBounds(c,r)){
			results.add(hexLine(c,r));
			if (c == 0){
				c = 1;
				r ++;
			}
			else{
				c = 0;
			}
		}
		for (int i = results.size()-1; i >= 0; i--)
			System.out.println(results.get(i));
	}

	/**
	 * Finds all coordinates with differences that have happened between time steps and displays them.
	 * @return An ArrayList containing coordinates of every difference
	 */
	public ArrayList<Coordinate> diffWorld(){
		readLock.lock();
		try {
			ArrayList<Coordinate> differences = new ArrayList<>();


			for (int i = 0; i < world.getColumns(); i++) {
				for (int j = 0; j < world.getRows(); j++) {

					if(i >= old.length || j >= old[0].length){ //out of bounds
						differences.add(new Coordinate(i,j));
					}
					else if (getEntityAt(i, j) == null && old[i][j] == null) {
						continue;
					}
					else if (getEntityAt(i, j) != old[i][j] || !(getEntityAt(i, j).equals(old[i][j]))) {
						//this should work unless we get a collision...
						//also check for nulls
						differences.add(new Coordinate(i, j));
					}

				}
			}
			old = world.getMap();


			return differences;
		}
		finally{
			readLock.unlock();
		}
	}

	/**
	 * Finds all the critters that have died between time steps and returns them.
	 * @return ArrayList of IDs of all critters that have died.
	 */
	public ArrayList<Integer> diffObituaries(){
		readLock.lock();
		try {
			ArrayList<Integer> deadCritters = new ArrayList<>();
			for (int i : oldCritters.keySet()) {
				if (!world.getCritters().containsKey(i)) {
					deadCritters.add(i);
				}
			}
			oldCritters = world.getCritters();
			return deadCritters;
		}
		finally {
			readLock.unlock();
		}
	}

	/**
	 * Returns a set of every coordinate that has had a change since {@param step} and now
	 * @param step update_since
	 * @return Set of every coordinate changed since update_since
	 */
	public Set<Coordinate> getDiffs(int step){
		readLock.lock();
		try {
			HashSet<Coordinate> diffCoords = new HashSet<>();
			for (int i = step; i < changes.size(); i++) {
				for (int j = 0; j < changes.get(i).size(); j++) {
					diffCoords.add(changes.get(i).get(j));
				}

			}

			return diffCoords;
		}
		finally {
			readLock.unlock();
		}
	}

	public Set<Integer> getObituaries(int step){
		readLock.lock();
		try{
			HashSet<Integer> deadCritters = new HashSet<>();
			for(int i = step; i < critterDeaths.size(); i++){
				for(int j = 0; j < critterDeaths.get(i).size(); j++){
					deadCritters.add(critterDeaths.get(i).get(j));
				}
			}
			return deadCritters;
		}
		finally {
			readLock.unlock();
		}
	}

	public int getTimesteps() {
		return timesteps;
	}
}