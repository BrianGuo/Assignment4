package ast;

public abstract class UnaryNode implements Node {
	
	abstract boolean hasChild();
	
	abstract void setChild(Node n);
	
	abstract Node getChild();
}