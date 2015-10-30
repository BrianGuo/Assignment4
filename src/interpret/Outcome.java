package interpret;


import ast.Expr;
import parse.TokenType;
import world.Critter;

/**
 * An example interface for representing an outcome of interpreting
 * a critter program.
 */
public interface Outcome {
    /**
     * Returns the critter whose outcome we must now evaluate
     * @return The critter inside the Outcome
     */
    public Critter getCritter();

    /**
     * Returns the action the critter has done, represented as a TokenType enum (because it already exists, might as well use it)
     * @return a TokenType object representing the action
     */
    public TokenType getAction();


    /**
     * Returns the expr contained in the outcome, or null if the action does not require one
     * @return evaluated expr
     */
    public Expr getExpr();

}
