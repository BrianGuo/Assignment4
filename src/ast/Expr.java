package ast;

/**
 * A critter program expression that has an integer value.
 */
public interface Expr extends Node {
	int evaluate();
	
	default boolean sameType(Node n){
		return (n instanceof Expr);
	}
}
