package ast;

import java.util.ArrayList;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 *
 */
public class BinaryCondition extends BinaryChildren implements Condition {

	Condition left;
	Condition right;
	Operator op;
	int leftsize;
	int rightsize;
	
    /**
     * Create an AST representation of l op r.
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
    	left = l;
    	this.op = op;
    	right = r;
    	leftsize = l.size();
    	rightsize = r.size();
    }

    public BinaryCondition(BinaryCondition b){
		this.left = b.left;
		this.right = b.right;
		this.op = b.op;
	}
    
    @Override
    public int size() {
        return leftsize + rightsize + 1;
    }

    @Override
    public Node nodeAt(int index) {
    	if (index == 0)
    		return this;
    	if (index < 0 || index >= size())
    		throw new IndexOutOfBoundsException();
    	else if (index > leftsize)
    		return right.nodeAt(index - (leftsize + 1));
    	else
    		return left.nodeAt(index - 1);
    }
    
    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
    	String condition = "";
        switch (op){
        case OR:
        	condition = "or";
        	break;
        case AND:
        	condition = "and";
        	break;
        default:
        	break;
        }
        return sb.append("{" + left.toString() + " " + condition + " " + right.toString() + "}");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return prettyPrint(sb).toString();
    }

    /**
     * An enumeration of all possible binary condition operators.
     */
    public enum Operator {
        OR, AND;
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
		if (r instanceof Condition)
			right = (Condition) r;
	}*/
	
	
	
}
