package ast;

import java.util.ArrayList;

import parse.Token;

/**
 * Represents a typical relation between two arithmetic expressions (<=, !=, >, etc).
 */
public class Relation extends BinaryChildren implements Condition,Tokenable {

	private Expr left;
	private Expr right;
	private Token R;
	//int leftsize;
	//int rightsize;
	
	public Relation (Expr l, Expr r, Token rel) {
		left = l;
		right = r;
		if (rel.isRelOp())
			R = rel;
		//leftsize = l.size();
		//rightsize = r.size();
	}
	public Relation(Relation b){
		this.left = b.getLeft();
		//leftsize = left.size();
		this.right = b.getRight();
		//rightsize = right.size();
		if (b.getR().isRelOp())
			this.R = b.getR();
	}
	@Override
	public int size() {
    	int size = 1;
    	if (left != null)
    		size += left.size();
    	if (right != null)
    		size += right.size();
        return size;
    }

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		else if (index > left.size())
			return right.nodeAt(index - (left.size()+1));
		else
			return left.nodeAt(index -1 );
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(left.toString() + " " + R.toString() + " " + right.toString());
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
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
		if (l instanceof Expr){
			left = (Expr) l;
			//leftsize = l.size();
		}
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Expr){
			right = (Expr) r;
			//rightsize = r.size();
		}
	}
	
	public Token getR(){
		return R;
	}
	@Override
	public boolean sameType(Node n) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Token getToken() {
		return getR();
	}
	@Override
	public void setToken(Token t) {
		if (t.isRelOp())
			R = t;
	}
}