package ast;

import java.util.ArrayList;

public class ActionNode extends UnaryNode implements Node {

	Action type;
	Expr num;
	int size;
	
	public ActionNode(Action a, Expr r){
		type = a;
		num = r;
		size = 1;
	}
	
	public ActionNode(Action a) {
		type = a;
		num = null;
		size = 1 + num.size();
	}
	public ActionNode(ActionNode a) {
		type = a.getAction();
		if (a.getNum() != null)
			num = a.getNum();
	}
	
	public Expr getNum() {
		return num;
	}
	public Action getAction(){
		return type;
	}
	@Override
	public int size() {
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
		String possibleEnd = "";
		if (type.equals(Action.TAG) || type.equals(Action.SERVE))
			possibleEnd = "[" + num.toString() + "]";
		return sb.append(type + possibleEnd);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	
	public enum Action {
		WAIT,FORWARD,BACKWARD,LEFT,RIGHT,EAT,ATTACK,GROW,BUD,MATE,TAG,SERVE;
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
		if (n instanceof Expr)
			num = ( Expr) n;
	}
	
	@Override
	public Node getChild(){
		return num;
	}
}