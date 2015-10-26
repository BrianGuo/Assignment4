package world;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorldTest {
    World w;

    @Before
    public void setUp() throws Exception {
        w = Factory.getRandomWorld();
    }

    @Test
    public void testHex() throws Exception {

    }

    @Test
    public void testParseWorld() throws Exception {

    }

    @Test
    public void testInBounds() throws Exception {

    }

    @Test
    public void testEvaluate() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        w.add(new Rock(4,5 ));
        assertTrue(w.getCritters().isEmpty());
        assertTrue(w.hexAt(4,5) instanceof Rock);
        assertTrue(w.hexAt(4,5) == w.hexAt(new Coordinate(4,5)));
    }
}