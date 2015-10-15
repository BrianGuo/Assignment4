package ast;

public abstract class UnaryNode implements Node {
	
	boolean hasChild(){return false;}
	
	void setChild(Node n){}
	
	Node getChild(){return null;};
}