package simulator;

import world.*;

import java.io.FileReader;
import java.util.LinkedList;

import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.*;
import interpret.CritterInterpreter;

public class Simulator {
	
	Interpreter interpreter;
	World world;
	
	public Simulator (World w, Interpreter i) {
		interpreter = i;
		world = w;
		((CritterInterpreter)interpreter).setWorld(world);
	}
	public Simulator (Interpreter i) {
		interpreter = i;
	}
	public void advance(int n) throws MissingElementException {
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
			throw new MissingElementException("World");
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
	public void parseWorld(FileReader f){
		try {
			this.world = Factory.getWorld(f);
			if (interpreter != null)
				((CritterInterpreter) interpreter).setWorld(world);
		}
		catch (SyntaxError e) {
			System.out.println("Your world file has syntax errors");
		}
		
	}
	
	public boolean hasWorld() {
		return (world != null);
	}
	public void putCritterRandomly(String filename) {
		Critter c = Factory.getCritter(filename);
		world.addRandom(c);
	}
}