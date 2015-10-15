package ast;

import java.util.ArrayList;

public class UpdateNode extends BinaryChildren implements Node {

	private MemoryNode left;
	private Expr right;
	private int size;
	private int leftsize;
	private int rightsize;
	
	public UpdateNode(MemoryNode l, Expr r) {
		left = l;
		right = r;
		leftsize = l.size();
		rightsize = r.size();
		size = leftsize + rightsize;
	}
	
	public UpdateNode(UpdateNode u) {
		left = u.getLeft();
		right = u.getRight();
	}
	
	public MemoryNode getLeft() {
		return left;
	}
	public Expr getRight(){
		return right;
	}
	public void setLeft(MemoryNode n) {
		left = n;
	}
	public void setRight(Expr r){
		right = r;
	}
	@Override
	public int size() {
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		else if (index > leftsize){
			int n = index-(leftsize+1);
			return right.nodeAt(n);
		}
		else {
			return left.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(left.toString() + " := " + right.toString());
	}
	
	public String toString() {
		StringBuilder temp = new StringBuilder();
		return prettyPrint(temp).toString();
	}

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if (left != null)
			temp.add(left);
		if (right != null)
			temp.add(right);
		return temp;
	}

	@Override
	public boolean sameType(Node n) {
		return (n instanceof UpdateNode);
	}

}