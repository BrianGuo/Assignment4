package ast;

/**
 * An interface representing a Boolean condition in a critter program.
 *
 */
public interface Condition extends Node {
	
	default boolean sameType(Node n){
		return (n instanceof Condition);
	}
}
