package ast;

import java.util.ArrayList;

/**
 * A representation of a critter rule.
 */
public class Rule extends BinaryChildren implements Node {

	private Condition left;
	private Command right;
	//private int leftsize;
	//private int rightsize;
	
	public Rule(Condition l, Command r) {
		left = l;
		right = r;
		//leftsize = l.size();
		//rightsize = r.size();
	}
	public Rule(Rule b){
		this.left = b.left;
		this.right = b.right;
	}
	
    @Override
    public int size() {
        return left.size() + right.size() + 1;
    }

    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index >= size())
        	throw new IndexOutOfBoundsException();
        else if (index > left.size()){
        	return right.nodeAt(index - (left.size()+1));
        }
        else
        	return left.nodeAt(index - 1);
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        return sb.append(left.toString() + " --> " + right.toString());
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
	
	@Override
	public Node getLeft() {
		return left;
	}

	@Override
	public Node getRight() {
		return right;
	}

	@Override
	public void setLeft(Node l) {
		if (l instanceof Condition){
			left = (Condition) l;
			//leftsize = l.size();
		}
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Command){
			right = (Command) r;
			//rightsize = r.size();
		}
	}
}
