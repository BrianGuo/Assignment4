package ast;

public class BinaryOp implements Expr {

	private String Operation;
	private Expr left;
	private int leftsize;
	private Expr right;
	private int rightsize;
	
	public BinaryOp(Expr l, Expr r, String o){
		if ("+-*/mod".indexOf(o.trim()) == -1)
			Operation = null;
		else{
			left = l;
			right = r;
			leftsize = l.size();
			rightsize = r.size();
			Operation = o;
		}
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
			int n = index-(leftsize+1);
			return right.nodeAt(n);
		}
		else {
			return left.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(left.toString() + Operation + right.toString());
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
		case "+":
			return a + b;
		case "-":
			return a-b;
		case "*":
			return a * b;
		case"/":
			return a/b;
		case"mod":
			return a%b;
		default:
			break;
		}
		return 0;
	}
	
}