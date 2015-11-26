package ast;

/**
 * For any type of Node which has two children.
 */
public abstract class BinaryChildren implements Node {
	
	Node left;
	Node right;
	
	public BinaryChildren(){}
	
	public BinaryChildren(Node left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public BinaryChildren(BinaryChildren b){
		left = b.left;
		right = b.right;
	}
	
	Node getLeft(){
		return left;
	}
	Node getRight(){
		return right;
	}
	
	void setLeft(Node l){
		left = l;
	}
	void setRight(Node r){
		right = r;
	}
	
	
}