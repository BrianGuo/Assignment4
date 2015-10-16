package ast;

/**
 * A Node that has a single child, such as the unary negation operator, the memory operator, and some sensors such as ahead.
 */

public abstract class UnaryNode implements Node {

	abstract boolean hasChild();
	
	abstract void setChild(Node n);
	
	abstract Node getChild();
}