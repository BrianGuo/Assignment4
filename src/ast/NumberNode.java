package ast;

public class NumberNode implements Expr{

	private int num;
	
	public NumberNode(int n){
		num = n;
	}
	@Override
	public int size() {
		return 1;
	}

	@Override
	public Node nodeAt(int index) {
		if (index != 0)
			throw new IndexOutOfBoundsException();
		else
			return this;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		return sb.append(this.num);
	}
	
	public String toString() {
		return Integer.toString(this.num);
		
	}
	
	public int evaluate() {
		return num;
	}
	
}