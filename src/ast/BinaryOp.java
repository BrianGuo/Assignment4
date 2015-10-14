package ast;

import java.util.ArrayList;

public class BinaryOp extends BinaryChildren implements Expr {

	private Operation Operation;
	private Expr left;
	private int leftsize;
	private Expr right;
	private int rightsize;
	
	public BinaryOp(BinaryOp b){
		this.left = b.left;
		this.right = b.right;
		this.Operation = b.Operation;
	}
	
	public BinaryOp(Expr l, Expr r, Operation o){
		left = l;
		right = r;
		leftsize = l.size();
		rightsize = r.size();
		Operation = o;
	}
	@Override
	public int size() {
		return leftsize + rightsize + 1;
	}

	@Override
	public Node nodeAt(int index) {
		if (index >= size() || index < 0)
			throw new IndexOutOfBoundsException();
		if (index == 0)
			return this;
		else if (index > leftsize) {
			return right.nodeAt(index - (leftsize+1));
		}
		else {
			return left.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		String op = "";
		switch (Operation) {
		case PLUS:
			op = "+";
			break;
		case MINUS:
			op = "-";
			break;
		case MULT:
			op = "*";
			break;
		case DIV:
			op = "/";
			break;
		case MOD:
			op = "mod";
			break;
		default:
			break;
		}
		return sb.append("(" + left.toString() + op + right.toString() + ")");
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		return prettyPrint(s).toString();
	}
	
	public int evaluate() {
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
	}
	
	public enum Operation {
		PLUS, MINUS, MULT, DIV, MOD;
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
		if (l instanceof Expr)
			left = (Expr) l;
	}

	@Override
	public void setRight(Node r) {
		if (r instanceof Expr)
			right = (Expr) r;
	}
	

}