package ast;

import critter.Critter;
import world.World;

/**
 * A critter program expression that has an integer value.
 */
public interface Expr extends Node {
	
	int evaluate(Critter c, World w);
	
	default boolean sameType(Node n){
		return (n instanceof Expr);
	}
}
