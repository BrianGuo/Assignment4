package tests;

import exceptions.IllegalCoordinateException;
import org.junit.Before;
import org.junit.Test;
import world.*;

import static org.junit.Assert.*;

public class WorldTest {
    World w;

    @Before
    public void setUp() throws Exception {
        w = Factory.getRandomWorld();
        Rock r = new Rock(4,5);
        w.add(r);
    }

    @Test
    public void testHex() throws Exception {
        assertTrue(w.hexAt(new Coordinate(4, 5)) instanceof Rock);
    }

    @Test
    public void testParseWorld() throws Exception {

    }

    @Test
    public void testInBounds() throws Exception {
        assertFalse(w.inBounds(-5, 5));
        assertFalse(w.inBounds(5, -5));
        assertFalse(w.inBounds(1000000, 5));
        assertFalse(w.inBounds(5, 100000));
        assertFalse(w.inBounds(w.getColumns(), w.getRows()));
        assertTrue(w.inBounds(w.getColumns() - 1, w.getRows() - 1));
        assertTrue(w.inBounds(4,5));
    }

    @Test
    public void testEvaluate() throws Exception {

    }

    @Test
    public void testGetters() throws Exception {
        assertTrue(w.getCritters().isEmpty());
        assertTrue(w.hexAt(4, 5) instanceof Rock);
        assertTrue(w.hexAt(4,5).appearance() == 0);
        assertTrue(w.hexAt(4, 5).toString().equals("#"));
        assertTrue(w.hexAt(4, 5) == w.hexAt(new Coordinate(4, 5)));
    }

    @Test(expected= IllegalArgumentException.class)
    public void testNegativeFood() throws Exception {
        Food f = new Food(4, 5, -5);
    }

    @Test(expected= IllegalCoordinateException.class)
    public void testNegativeCoords() throws Exception {
        Food f = new Food(-4, 5, 5);
    }

    //TODO: replace with ExpectedException
    @Test(expected= IllegalCoordinateException.class)
    public void testFullUnoccupied() throws Exception {
        for(int i = 0; i < w.getColumns(); i++){
            for(int j = 0; j < w.getRows(); j++){
                w.add(new Rock(i,j));
            }
        }
        w.getRandomUnoccupiedLocation();
    }

    @Test
    public void testOverlap() throws Exception{
        Food f = new Food(4,5, 10);
        w.add(f);
        assertFalse(w.hexAt(4, 5) instanceof Food);
        assertTrue(w.hexAt(4,5) instanceof Rock);
    }

    @Test
    public void testGetUnoccupied() throws Exception{
        for(int i = 0; i < 1000; i++) {
            Coordinate c = w.getRandomUnoccupiedLocation();
            assertNull(w.hexAt(c));
        }
    }

    @Test
    public void testClean() throws Exception{
        Food f = new Food(4,6,10);
        w.add(f);
        assertTrue(w.hexAt(4,6) instanceof Food);
        w.clean(f);
        assertNull(w.hexAt(4,6));
    }

    //TODO: write a similar one for kill()
}