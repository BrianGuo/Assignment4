package ast;

/**
 * An interface representing a Boolean condition in a critter program.
 *
 */
import world.Critter;
import world.World;

public interface Condition extends Node {
	
	default boolean sameType(Node n){
		return (n instanceof Condition);
	}
	boolean evaluate(Critter c, World w);
}
