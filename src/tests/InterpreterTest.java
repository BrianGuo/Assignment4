package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

import org.junit.Test;

import ast.Condition;
import ast.Expr;
import ast.Program;
import ast.Rule;
import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import parse.Parser;
import parse.ParserFactory;
import world.Critter;
import world.Factory;
import world.World;

public class InterpreterTest {

	@Test
	public void testInterpret() {
		fail("Not yet implemented");
	}

	@Test
	public void testEvalCondition() {
		
		CritterInterpreter i = new CritterInterpreter();
		Parser p = ParserFactory.getParser();
		try{
			FileReader f = new FileReader("examples/example-rules.txt");
			Program prog = p.parse(f);
			int[] attributes = new int[8];
			Critter cr = new Critter(attributes, 0, "this");
			f = new FileReader("examples/world.txt");
			World w = Factory.getWorld(f);
			Rule r = prog.getRules().get(0);
			Condition c = ((Condition)r.getLeft());
			i.setCritter(cr);
			i.setWorld(w);
			assertFalse(i.eval(c));
		}
		catch(FileNotFoundException e){
			fail();
		}
		catch(SyntaxError e){
			fail();
		}
	}

	@Test
	public void testEvalExpr() {
		CritterInterpreter i = new CritterInterpreter();
		Parser p = ParserFactory.getParser();
		try{
			FileReader f = new FileReader("examples/example-rules.txt");
			Program prog = p.parse(f);
			int[] attributes = new int[11];
			attributes[5] = 50;//Result should be 50
			Critter cr = new Critter(attributes, 0, "this");
			f = new FileReader("examples/world.txt");
			World w = Factory.getWorld(f);
			Rule r = prog.getRules().get(2);
			Expr e = (Expr) prog.nodeAt(3);
			i.setCritter(cr);
			i.setWorld(w);
			System.out.println(e);
			System.out.println(i.eval(e));
			System.out.println(Arrays.toString(attributes));
			i.setCritter(null);
			i.setWorld(null);
			assertNull(i.eval(e));
		}
		catch(FileNotFoundException e){
			fail();
		}
		catch(SyntaxError e){
			fail();
		}
	}

	@Test
	public void testPerform() {
		fail("Not yet implemented");
	}

}
