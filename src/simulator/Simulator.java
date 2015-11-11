package simulator;



import world.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.*;
import interpret.CritterInterpreter;



public class Simulator {
	
	Interpreter interpreter;
	public World world;
	int timesteps;
	Entity[][] old;

	
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
		timesteps = 0;
		old = new Entity[getWorldColumns()][getWorldRows()];
	}
	
	/**
	 * Stores an interpreter, used when no world yet provided
	 * @param i    the interpreter to store 
	 */
	public Simulator (Interpreter i) {
		interpreter = i;
		timesteps = 0;
	}

	/**
	 * advances the world a number of timesteps
	 * @param n   the number of timesteps to advance the world
	 */
	public void advance(int n){
		if (hasWorld()){
			for (int i =0; i< n; i++ ){
				LinkedList<Critter> copy = new LinkedList<Critter>();
				copy.addAll(world.getCritters());
				for (Critter c: copy){
					if (!(world.getCritters().contains(c)))
						continue;
					((CritterInterpreter) interpreter).setCritter(c);
					Outcome o = interpreter.interpret(c.getProgram());
					world.evaluate(o);
					world.judge(c);
				}
				timesteps ++;
			}
		}
		else{
			System.out.println("You haven't loaded a world");
		}
	}
	
	/**
	 * Sets the world field to the parameter
	 * @param w    the world to be set to
	 */
	public void setWorld(World w){
		world = w;
		if (interpreter != null)
			((CritterInterpreter)interpreter).setWorld(w);
		old = new Entity[getWorldColumns()][getWorldRows()];
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

	public void parseWorld(FileReader f){
		try {
			this.world = Factory.getWorld(f);
			if (interpreter != null)
				((CritterInterpreter) interpreter).setWorld(world);
			old = new Entity[getWorldColumns()][getWorldRows()];
		}
		catch (SyntaxError e) {
			System.out.println("Your world file has syntax errors");
		}
		catch(NoSuchElementException e) {
			System.out.println("There's something REALLY off about your file");
		}
		catch (FileNotFoundException e) {
			System.out.println("Your File Was Not Found");
		}
	}
	
	/**
	 * Returns the timesteps field
	 * @return   the number of timesteps taken
	 */
	public int getTimesteps() {
		return timesteps;
	}
	
	/**
	 * Returns the number of critters in the world
	 * @return    the number of critters in the world
	 */
	public int getNumCritters() {
		return world.getCritters().size();
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
		return world.hexAt(col, row);
	}

	/**
	 * Returns the number of columns in the underlying world, or 0 if a world has not been loaded yet.
	 * @return # of cols in the world, or 0 if world == null
	 */
	public int getWorldColumns(){
		if(hasWorld())
			return world.getColumns();
		return 0;
	}

	/**
	 * Returns the number of rows in the underlying world, or 0 if a world has not been loaded yet.
	 * @return # of rows in the world, or 0 if world == null
	 */
	public int getWorldRows(){
		if(hasWorld())
			return world.getRows();
		return 0;
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
		if (!hasWorld())
			throw new MissingElementException();
		for (int i = 0 ; i < n; i++ ){
			Critter c = Factory.getCritter(filename,world.constants);
			world.addRandom(c);
		}
	}

	/**
	 * Adds an entity to the world at a random location
	 * Had to create a new function since putCritterRandomly...only works on critters...and with strings.
	 * @param entity Entity to be added
	 */
	public void addRandomEntity(Entity entity){
		world.addRandom(entity);
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
	public void hexLocation(int c, int r) {
		Entity e = world.hexAt(c, r);
		if (!world.inBounds(c, r))
			System.out.println("This coordinate is out of bounds");
		else if (e == null)
			System.out.println("There is nothing at this coordinate");
		else if (e instanceof Rock)
			System.out.println("This coordinate contains a rock");
		else if (e instanceof Food) {
			System.out.println("This coordinate contains food");
			System.out.println("The amount of food is: " + ((Food)e).getAmt());
		}
		else {
			assert(e instanceof Critter);
			Critter cr = (Critter) e;
			System.out.println("This coordinate contains a critter");
			System.out.println("This critter is of species: " +cr.getSpecies());
			for (int i = 0; i < cr.getAttributeAtIndex(0); i++) {
				System.out.println("The attribute at index " + i + " is the value " + cr.getAttributeAtIndex(i));
			}
			System.out.println("The program is:\n" + cr.getProgram().toString());
			System.out.println("The last Rule executed was" + cr.getLastRule().toString());
		}
	}

	/**
	 * Returns entity at coordinate in the world.  Wrapper for world method.
	 */
	public Entity getEntityAt(Coordinate coordinate){
		return world.hexAt(coordinate);
	}

	/**
	 * Adds an entity to the world
	 * @param entity Entity to be added, with the correct coordinate
	 */
	public void addEntity(Entity entity){
		world.add(entity);
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
	 * Finds all the differences that have happened between time steps and displays them.
	 * @return An ArrayList containing coordinates of every difference
	 */
	public ArrayList<Coordinate> diffWorld(){
		//TODO deal with changing attributes--use hashCode()?
		
		ArrayList<Coordinate> differences = new ArrayList<>();
		for(int i = 0; i < world.getColumns(); i ++){
			for(int j = 0; j < world.getRows(); j++){
				if(getEntityAt(i,j) != old[i][j] || !(getEntityAt(i, j).equals(old[i][j]))){
					//this should work unless we get a collision...
					//also check for nulls
					differences.add(new Coordinate(i,j));
				};
			}
		}
		old = world.getMap();
		return differences;
	}

}