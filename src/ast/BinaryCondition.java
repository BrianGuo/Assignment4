package ast;

import java.util.ArrayList;

import critter.Critter;
import parse.Token;
import parse.TokenType;
import world.World;

/**
 * A representation of a binary Boolean condition: 'and' or 'or'
 *
 */
public class BinaryCondition extends BinaryChildren implements Condition, Tokenable {

	Condition left;
	Condition right;
	Token op;
	//int leftsize;
	//int rightsize;
	
    /**
     * Create an AST representation of l op r.
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Token op, Condition r) {
    	left = l;
    	if (isOperator(op))
    		this.op = op;
    	right = r;
    	//leftsize = l.size();
    	//rightsize = r.size();
		//System.out.println("op:" + this.op);
    }

    public boolean isOperator(Token t){
    	return (t.getType() == TokenType.OR || t.getType() == TokenType.AND);
    }
    
    public BinaryCondition(BinaryCondition b){
		this.left = b.left;
		this.right = b.right;
		if(isOperator(b.getOp()))
			this.op = b.op;
		//leftsize = left.size();
		//rightsize = right.size();
	}
    public Token getOp(){
    	return op;
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
    	if (index < 0 || index >= size())
    		throw new IndexOutOfBoundsException();
    	else if (index > left.size())
    		return right.nodeAt(index - (left.size() + 1));
    	else
    		return left.nodeAt(index - 1);
    }
    
    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
		//System.out.println("op in pp:" + op);
//		System.out.println("l:" + left);
//		System.out.println("r:" + right);
//		System.out.println("op:" + op);
        return sb.append("{" + left.toString() + " " + op.toString() + " " + right.toString() + "}");
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
			//leftsize = left.size();
		}
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Condition){
			right = (Condition) r;
			//rightsize = right.size();
		}
	}


	@Override
	public Token getToken() {
		return getOp();
	}

	@Override
	public void setToken(Token t) {
		if(isOperator(t))
			op = t;
	}
	
	@Override
	public boolean evaluate(Critter c, World w) {
		switch(op.getType()) {
		case AND:
			return (left.evaluate(c, w) && right.evaluate(c,w));
		case OR:
			return (left.evaluate(c,w) || right.evaluate(c,w));
		default:
			return false;
		}
	}
	
	
}
