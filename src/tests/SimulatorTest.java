package tests;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;

import org.junit.Test;

import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import interpret.Interpreter;
import simulator.Simulator;
import world.Factory;
import world.World;

public class SimulatorTest {

	@Test
	public void testSimulatorWorldInterpreter() throws SyntaxError {
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
		}
		World w = Factory.getWorld(f);
		Interpreter i = new CritterInterpreter();
		Simulator s = new Simulator(w,i);
		World w2 = null;
		Interpreter i2 = null;
		Simulator s2 = new Simulator (w2,i2);
	}

	@Test
	public void testSimulatorInterpreter() {
		CritterInterpreter i = new CritterInterpreter();
		Simulator s = new Simulator(i);
		CritterInterpreter i2 = null;
		Simulator s2 = new Simulator (i2);
	}

	@Test
	public void testAdvance() {
	}

	@Test
	public void testSetWorld() throws SyntaxError {
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
		}
		World w = Factory.getWorld(f);
		Simulator s = new Simulator();
		s.setWorld(w);
		World w2 = null;
		s.setWorld(w2);
	}

	@Test
	public void testSetInterpreter() {
		CritterInterpreter c = new CritterInterpreter();
		Simulator s = new Simulator();
		s.setInterpreter(c);
		CritterInterpreter d = null;
		s.setInterpreter(d);
	}

	@Test
	public void testParseWorld() {
		Simulator s = new Simulator();
		s.parseWorld("examples/world.txt");
	}

	@Test
	public void testHasWorld() throws SyntaxError {
		Simulator s = new Simulator();
		assertFalse(s.hasWorld());
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
		}
		World w = Factory.getWorld(f);
		s.setWorld(w);
		assertTrue(s.hasWorld());
		
	}

	@Test
	public void testPutCritterRandomly() {
	}
	
	@Test
	public void testHexWorld() throws SyntaxError{
		Simulator s = new Simulator();
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
		}
		World w = Factory.getWorld(f);
		s.setWorld(w);
		s.hexWorld();
	}

}
