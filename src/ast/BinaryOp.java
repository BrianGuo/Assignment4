package ast;

import java.util.ArrayList;

import parse.Token;

public class BinaryOp extends BinaryChildren implements Expr, Tokenable {

	private Token Operation;
	private Expr left;
	//private int leftsize;
	private Expr right;
	//private int rightsize;
	
	public BinaryOp(BinaryOp b){
		this.left = b.left;
		//leftsize = b.getLeft().size();
		this.right = b.right;
		//rightsize = b.getRight().size();
		if (b.getOperation().isAddOp() || b.getOperation().isMulOp())
			this.Operation = b.getOperation();
	}
	
	public BinaryOp(Expr l, Expr r, Token o){
		left = l;
		right = r;
		//leftsize = l.size();
		//rightsize = r.size();
		if (o.isAddOp() || o.isMulOp())
			Operation = o;
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
		if (index >= size() || index < 0)
			throw new IndexOutOfBoundsException();
		if (index == 0)
			return this;
		else if (index > left.size()) {
			return right.nodeAt(index - (left.size()+1));
		}
		else {
			return left.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append("(" + left.toString() + Operation.toString() + right.toString() + ")");
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		return prettyPrint(s).toString();
	}
	
	/*public int evaluate() {
		int a = 0;
		int b = 0;
		try{
			a = left.evaluate();
			b = right.evaluate();
		}
		catch (ArithmeticException e){
			System.out.println("You've divided by zero. Just letting you know");
			return 0;
		}
		switch(Operation){
		case PLUS:
			return a + b;
		case MINUS:
			return a-b;
		case MULT:
			return a * b;
		case DIV:
			return a/b;
		case MOD:
			return a%b;
		default:
			break;
		}
		return 0;
	}*/
	
	

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
		if (l instanceof Expr){
			left = (Expr) l;
			//leftsize = left.size();
		}
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Expr){
			right = (Expr) r;
			//rightsize = right.size();
		}
	}
	
	public Token getOperation(){
		return Operation;
	}


	@Override
	public Token getToken() {
		return getOperation();
	}

	@Override
	public void setToken(Token t) {
		if (t.isAddOp() || t.isMulOp())
			Operation = t;
	}
	

}