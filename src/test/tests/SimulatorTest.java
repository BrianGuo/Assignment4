package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.junit.Test;

import exceptions.MissingElementException;
import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import interpret.Interpreter;
import simulator.Simulator;
import world.Critter;
import world.Factory;
import world.World;

public class SimulatorTest {

	@Test
	public void testSimulatorWorldInterpreter() throws Exception {
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
			fail();
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
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
			World w = Factory.getWorld(f);
			Interpreter i = new CritterInterpreter();
			Simulator s = new Simulator(w,i);
			s.setInterpreter(i);
			s.setWorld(w);
			s.putCritterRandomly("example_critter.txt", 3);
			s.advance(5);
		}
		catch(Exception e){
			System.out.println(e.getClass());
			fail();
		}
	}

	@Test
	public void testSetWorld() throws Exception {
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
			fail();
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
	public void testHasWorld() throws Exception{
		Simulator s = new Simulator();
		assertFalse(s.hasWorld());
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
			fail();
		}
		World w = Factory.getWorld(f);
		s.setWorld(w);
		assertTrue(s.hasWorld());
		
	}

	@Test
	public void testPutCritterRandomly() {
		Simulator s = new Simulator();
		assertFalse(s.hasWorld());
		FileReader f = null;
		try{
			f = new FileReader("world.txt");
			World w = Factory.getWorld(f);
			s.setWorld(w);
			s.putCritterRandomly("spiralcritter.txt", 5);
		}
		catch(FileNotFoundException e){
			fail();
		}
		catch(SyntaxError e) {
			System.out.println("SyntaxError");
		}
		catch(MissingElementException e) {
			System.out.println("MissingElement");
		}
	}
	
	@Test
	public void testHexWorld() throws Exception{
		Simulator s = new Simulator();
		FileReader f = null;
		try{
			f = new FileReader("examples/world.txt");
		}
		catch(Exception e){
			fail();
		}
		World w = Factory.getWorld(f);
		s.setWorld(w);
		s.hexWorld();
	}

}
