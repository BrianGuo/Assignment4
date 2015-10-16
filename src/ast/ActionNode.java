package ast;

import java.util.ArrayList;

import parse.Token;
public class ActionNode extends UnaryNode implements Node,Tokenable {
	/**
	 * A Node that represents an Action.
	 * {@code type} Represents the specific type of action--attack, wait, etc
	 * {@code num} For use in tag and serve
	 */
	Token type;
	Expr num;
	//int size;
	
	public ActionNode(Token a, Expr r){
		if (a.isAction())
			type = a;
		num = r;
		//size = 1+num.size();
	}
	
	public ActionNode(Token a) {
		if (a.isAction())
			type = a;
		num = null;
		//size = 1;
	}

	public ActionNode(ActionNode a) {
		type = a.getAction();
		if (a.getNum() != null)
			num = a.getNum();
	}
	
	public Expr getNum() {
		return num;
	}
	public Token getAction(){
		return type;
	}
	@Override
	public int size() {
		int size = 1;
		if (num!= null)
			size += num.size();
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		if (index < 0 || (num != null && index > num.size())|| (num == null && index > 0))
			throw new IndexOutOfBoundsException();
		else {
			return num.nodeAt(index-1);
		}
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		sb.append(type);
		if (num != null)
			sb.append("[" + num.toString() + "]");
		return sb;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if (num != null)
			temp.add(num);
		return temp;
	}

	@Override
	public boolean sameType(Node n) {
		return (n instanceof ActionNode);
	}
	
	@Override
	public boolean hasChild() {
		return (num != null);
	}
	
	@Override
	public void setChild(Node n) {
		if (n instanceof Expr){
			num = ( Expr) n;
			//size = 1 + num.size();
		}
	}
	
	@Override
	public Node getChild(){
		return num;
	}

	@Override
	public Token getToken() {
		return getAction();
	}

	@Override
	public void setToken(Token t) {
		if(t.isAction())
			type = t;
	}
}
