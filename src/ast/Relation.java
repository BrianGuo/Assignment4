package ast;

import java.util.ArrayList;

import parse.Token;

public class Relation extends BinaryChildren implements Condition {

	private Expr left;
	private Expr right;
	private Token R;
	int leftsize;
	int rightsize;
	
	public Relation (Expr l, Expr r, Token rel) {
		left = l;
		right = r;
		if (rel.isRelOp())
			R = rel;
		leftsize = l.size();
		rightsize = r.size();
	}
	public Relation(Relation b){
		this.left = b.getLeft();
		leftsize = left.size();
		this.right = b.getRight();
		rightsize = right.size();
		if (b.getR().isRelOp())
			this.R = b.getR();
	}
	@Override
	public int size() {
		return leftsize + rightsize + 1;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		else if (index > leftsize)
			return right.nodeAt(index - (leftsize+1));
		else
			return left.nodeAt(index -1 );
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(left.toString() + " " + R.toString() + " " + right.toString());
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
	public Expr getLeft() {
		return left;
	}

	@Override
	public Expr getRight() {
		return right;
	}

	@Override
	public void setLeft(Node l) {
		if (l instanceof Expr)
			left = (Expr) l;
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Expr)
			right = (Expr) r;
	}
	
	public Token getR(){
		return R;
	}
}