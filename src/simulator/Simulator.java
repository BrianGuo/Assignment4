package simulator;

import world.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.*;
import interpret.CritterInterpreter;

public class Simulator {
	
	Interpreter interpreter;
	World world;
	
	public Simulator() {}
	public Simulator (World w, Interpreter i) {
		interpreter = i;
		world = w;
		if (interpreter != null)
			((CritterInterpreter)interpreter).setWorld(world);
	}
	public Simulator (Interpreter i) {
		interpreter = i;
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
	
	public boolean hasWorld() {
		return (world != null);
	}
	public void putCritterRandomly(String filename) throws MissingElementException{
		Critter c = Factory.getCritter(filename);
		if (world == null)
			throw new MissingElementException();
		world.addRandom(c);
	}
}