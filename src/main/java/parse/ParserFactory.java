package parse;

import java.io.Reader;

/**
 * A factory that gives access to instances of parser.
 */
public class ParserFactory {

    /**
     * Return a {@code Parser} that can parse critter programs.
     * @return A critter program parser
     */
    public static Parser getParser() {
        return new ParserImpl();
    }

    /**
     * Return a parser that can parse Critter files.
     * @return A Critter text parser
     */
    public static CritterParser getCritterParser(){
        return new CritterParser();
    }

    /**
     * Return a parser that can parse World files.
     * @return A world text parser.
     */
    public static WorldParser getWorldParser(){
        return new WorldParser();
    }
}
