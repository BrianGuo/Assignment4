package ast;

import java.util.ArrayList;

public class Sensor extends UnaryNode implements Expr {

	private Senses s;
	private Expr r;
	private int size;
	
	public Sensor(Senses s){
		this.s= s;
		r = null;
		size = 1;
	}
	
	public Sensor (Sensor sense){
		s = sense.getSense();
		if(sense.getExpr()!= null)
			r = sense.getExpr();
	}
	public Senses getSense(){
		return s;
	}
	public Expr getExpr() {
		return r;
	}
	public Sensor(Senses s, Expr r){
		this.s = s;
		if(s.equals(Senses.NEARBY) || s.equals(Senses.AHEAD) || s.equals(Senses.RANDOM)){
			this.r = r;
		}
		size = r.size() + 1;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index ==0)
			return this;
		else if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		else{
			return r.nodeAt(index -1);
		}
		
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		String suffix = "";
		if (r != null)
			suffix = "[" + r.toString() + "]";
		String prefix = "";
		switch(s) {
		case NEARBY:
			prefix = "nearby";
			break;
		case AHEAD:
			prefix = "ahead";
			break;
		case RANDOM:
			prefix = "random";
			break;
		case SMELL:
			prefix = "smell";
			break;
		default:
			break;
		}
		sb.append(prefix + suffix);
		return sb;
	}

	@Override
	public int evaluate() {
		return 0;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	
	public enum Senses {
		NEARBY,AHEAD,RANDOM,SMELL;
	}

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if(r != null)
			temp.add(r);
		return temp;
	}
	
	@Override
	public boolean hasChild() {
		return ((s == Senses.NEARBY || s == Senses.AHEAD || s == Senses.RANDOM) && r != null);
	}
	
	@Override
	public void setChild(Node n) {
		if (n instanceof Expr)
			r = (Expr) n;
	}

	@Override
	public Node getChild(){
		return r;
	}
}