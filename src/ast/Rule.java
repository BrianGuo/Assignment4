package ast;

import java.util.ArrayList;

/**
 * A representation of a critter rule.
 */
public class Rule extends BinaryChildren implements Node {

	private Condition left;
	private Command right;
	private int leftsize;
	private int rightsize;
	
	public Rule(Condition l, Command r) {
		left = l;
		right = r;
		leftsize = l.size();
		rightsize = r.size();
	}
	public Rule(Rule b){
		this.left = b.left;
		this.right = b.right;
	}
	
    @Override
    public int size() {
        return leftsize + rightsize + 1;
    }

    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index >= size())
        	throw new IndexOutOfBoundsException();
        else if (index > leftsize)
        	return right.nodeAt(index - (leftsize+1));
        else
        	return left.nodeAt(index - 1);
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        return sb.append(right.toString() + " --> " + left.toString());
    }
    
    @Override
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
	public boolean sameType(Node n) {
		return (n instanceof Rule);
	}
	
	/*@Override
	public Object getLeft() {
		return left;
	}

	@Override
	public Object getRight() {
		return right;
	}

	@Override
	public void setLeft(Object l) {
		if (l instanceof Condition)
			left = (Condition) l;
	}

	@Override
	public void setRight(Object r) {
		if (r instanceof Command)
			right = (Command) r;
	}*/
}
