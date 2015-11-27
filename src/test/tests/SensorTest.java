package tests;

import static org.junit.Assert.*;

import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import ast.Sensor;
import parse.Token;
import parse.TokenType;
import world.Critter;
import world.Factory;
import world.World;

public class SensorTest {

	World w;
    Critter c1;
    Critter c2;
	@Before
    public void setUp() throws Exception {
        w = Factory.getWorld(new FileReader("world.txt"));
        c1 = Factory.getCritter("example_critter.txt", "4", "5", "2", w.constants);
        c2 = Factory.getCritter("example_critter.txt", "5", "5", "5", w.constants);
        w.add(c1);
        w.add(c2);

    }
	@Test
	public void testSmell() {
		Critter crit = (Critter) w.hexAt(2,5);
		Critter crit2 = (Critter) w.hexAt(4,3);
		Sensor s = new Sensor(new Token(TokenType.SMELL, 0));
		try{
			System.out.println(s.evaluateSmell(crit,w));
			System.out.println(s.evaluateSmell(crit2, w));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
