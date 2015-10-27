package simulator;

import world.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;

import exceptions.IllegalCoordinateException;
import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.*;
import interpret.CritterInterpreter;

public class Simulator {
	
	Interpreter interpreter;
	World world;
	int timesteps;
	
	public Simulator() {}
	public Simulator (World w, Interpreter i) {
		interpreter = i;
		world = w;
		if (interpreter != null)
			((CritterInterpreter)interpreter).setWorld(world);
		timesteps = 0;
	}
	public Simulator (Interpreter i) {
		interpreter = i;
		timesteps = 0;
	}
	public void advance(int n){
		if (world != null){
			LinkedList<Critter> critters = world.getCritters();
			for (int i =0; i< n; i++ ){
				for (Critter c : critters){
					((CritterInterpreter) interpreter).setCritter(c);
					Outcome o = interpreter.interpret(c.getProgram());
					world.evaluate(o);
				}
			}
			timesteps ++;
		}
		else{
			System.out.println("You haven't loaded a world");
		}
	}
	public void setWorld(World w){
		world = w;
		if (interpreter != null)
			((CritterInterpreter)interpreter).setWorld(w);
	}
	
	public void setInterpreter(Interpreter i) {
		interpreter = i;
		if (world != null)
			((CritterInterpreter)interpreter).setWorld(world);
	}
	public void parseWorld(String filename){
		try {
			FileReader f = new FileReader(filename);
			this.world = Factory.getWorld(f);
			if (interpreter != null)
				((CritterInterpreter) interpreter).setWorld(world);
		}
		catch (SyntaxError e) {
			System.out.println("Your world file has syntax errors");
		}
		catch (FileNotFoundException e) {
			System.out.println("Your File was not found");
		}
		
	}
	public int getTimesteps() {
		return timesteps;
	}
	public int getNumCritters() {
		return world.getCritters().size();
	}
	public boolean hasWorld() {
		return (world != null);
	}
	public void putCritterRandomly(String filename) throws MissingElementException{
		Critter c = Factory.getCritter(filename);
		if (world == null)
			throw new MissingElementException();
		world.addRandom(c);
	}
	
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
	
	
	public ArrayList<String> hexWorld() {
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
		return results;
	}
}