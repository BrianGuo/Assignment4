package ast;

public class ActionNode implements Node {


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
}
