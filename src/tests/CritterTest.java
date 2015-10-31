package tests;

import ast.Sensor;
import org.junit.Before;
import org.junit.Test;
import parse.TokenType;
import world.*;

import java.io.FileReader;

import static org.junit.Assert.*;

public class CritterTest {
    //tests critter methods specifically
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
    public void testGetSpecies() throws Exception {

    }

    @Test
    public void testGetDirection() throws Exception {
        assertEquals(c1.getDirection(), 2);
        assertEquals(c2.getDirection(), 5);
    }

    @Test
    public void testAppearance() throws Exception {

    }

    @Test
    public void testTurn() throws Exception {
        c2.turn(TokenType.RIGHT);
        assertEquals(c2.getDirection(), 0);
    }

    @Test
    public void testConsumeEnergy() throws Exception {
        Coordinate oldLoc = c1.getLocation();
        c1.consumeEnergy(50000);
        assertTrue(c1.isDead());
        c2.consumeEnergy(500);
        assertTrue(c2.isDead());
    }


    @Test
    public void testEat() throws Exception {
        c2.setDirection(4);
        c2.consumeEnergy(50);
        c2.eat(w.hexAt(4, 4));
        assertEquals(c2.getAttributeAtIndex(4), 500);

        c2.consumeEnergy(400);
        c2.eat(w.hexAt(4, 4));
        assertTrue(((Food) w.hexAt(4, 4)).isConsumed());

    }

    @Test
    public void testAbsorb() throws Exception {
        c1.absorb();
        assertEquals(500, c1.getAttributeAtIndex(4));
        c1.consumeEnergy(499);
        c1.absorb();
        assertEquals(2, c1.getAttributeAtIndex(4));
        c1.consumeEnergy(2);
        assertTrue(c1.isDead());
    }

    @Test
    public void testDying() throws Exception {
        c1.consumeEnergy(999);
        Coordinate oldLoc = c1.getLocation();
        int foodAmt = c1.size() * w.constants.FOOD_PER_SIZE;
        w.judge(c1);
        assertTrue(w.hexAt(oldLoc) instanceof Food);
        assertEquals(foodAmt, ((Food) w.hexAt(oldLoc)).consume(5000));
    }

    @Test
    public void testServe() throws Exception {
        c1.serve(5, Sensor.coordAheadAt(c1, w, 1));
        assertEquals(494, c1.getAttributeAtIndex(4));
        c2.setDirection(3);
        c2.consumeEnergy(450);
        Food newFood = c2.serve(100, Sensor.coordAheadAt(c2, w, 1));
        assertTrue(newFood.getCol() ==5);
        assertEquals(50 - c2.size(), newFood.getAmt());
        w.add(newFood);
        assertTrue(w.hexAt(5,4) instanceof Food);
    }

    @Test
    public void testGrow() throws Exception {

    }

    @Test
    public void testTag() throws Exception {

    }

    @Test
    public void testAttack() throws Exception {

    }

    @Test
    public void testMove() throws Exception {

    }

    @Test
    public void testBud() throws Exception {

    }

    @Test
    public void testMate() throws Exception {

    }
}