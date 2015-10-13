package ast;

public class ActionNode implements Node {

	String type;
	Expr num;
	
	
	public ActionNode(String t, Expr r){
		type = t;
		num = r;
	}
	
	public ActionNode(String t) {
		type = t;
		num = null;
	}
	
	@Override
	public int size() {
		if (num != null)
			return num.size() +1;
		else
			return 1;
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
		if (type.equals("tag") || type.equals("serve"))
			possibleEnd = "[" + num.toString() + "]";
		return sb.append(type + possibleEnd);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	
	
}