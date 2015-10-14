package ast;

import java.util.ArrayList;

public class MemoryNode implements Expr {

	private Expr expression;
	private int location;
	private int size;
	
	
	public MemoryNode(Expr exp){
		expression = exp;
		location = expression.evaluate();
		size = expression.size()+1;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException();
		if (index == 0){
			return this;
		}
		else{
			return expression.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		String temp = "";
		switch (location){
		case 0:
			temp = "MEMSIZE";
			break;
		case 1:
			temp = "DEFENSE";
			break;
		case 2:
			temp = "OFFENSE";
			break;
		case 3:
			temp = "SIZE";
			break;
		case 4:
			temp = "ENERGY";
			break;
		case 5:
			temp = "PASS";
			break;
		case 6:
			temp = "TAG";
			break;
		case 7:
			temp = "POSTURE";
			break;
		default:
			temp = "mem[" + location + "]";
			break;
		}
		return sb.append(temp);
	}
	
	public String toString() {
		StringBuilder temp = new StringBuilder();
		return prettyPrint(temp).toString();
	}

	@Override
	public int evaluate() {
		return 0;
	}

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if (expression != null)
			temp.add(expression);
		return temp;
	}

	@Override
	public boolean sameType(Node n) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}