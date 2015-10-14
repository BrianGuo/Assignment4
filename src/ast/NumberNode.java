package ast;

import java.util.ArrayList;

public class NumberNode implements Expr{

	private int num;
	
	public NumberNode(int n){
		num = n;
	}
	
	public NumberNode(NumberNode n){
		num = n.getNum();
	}
	
	public int getNum(){
		return num;
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
	
	@Override
	public ArrayList<Node> children() {
		return new ArrayList<Node>();
	}
	
}