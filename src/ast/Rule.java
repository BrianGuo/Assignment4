package ast;

/**
 * A representation of a critter rule.
 */
public class Rule implements Node {

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
	
    @Override
    public int size() {
        return leftsize + rightsize + 1;
    }

    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index > size())
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
}
