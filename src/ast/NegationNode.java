package ast;

import java.util.ArrayList;

public class NegationNode extends UnaryNode implements Expr {
	/**
	 * Represents the unary negation operator.  Negates the value of {@code} expression.  Can be chained together for double or more negatives.
	 */

	private Expr expression;
	//private int size;
	
	public NegationNode(Expr e) {
		expression = e;
		//size = e.size();
	}
	
	public NegationNode(NegationNode n) {
		expression = n.getExpression();
		//size = n.size();
	}
	public Expr getExpression(){
		return expression;
	}
	@Override
	public int size() {
		return expression.size();
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		else{
			return expression.nodeAt(index -1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append("-" + expression.toString());
		return sb;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> result = new ArrayList<Node>();
		result.add(expression);
		return result;
	}

	
	@Override
	public boolean hasChild() {
		return (expression!= null);
	}
	
	@Override 
	public void setChild(Node n) {
		if(n instanceof Expr){
			expression = (Expr) n;
			//size = n.size()+1;
		}
	}
	
	@Override
	public Node getChild(){
		return expression;
	}
}