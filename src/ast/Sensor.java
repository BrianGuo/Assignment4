package ast;

import java.util.ArrayList;

import parse.Token;
import parse.TokenType;

public class Sensor extends UnaryNode implements Expr, Tokenable {

	private Token sense;
	private Expr r;
	private int size;
	
	public Sensor(Token s){
		this.sense= s;
		r = null;
		size = 1;
	}
	
	public Sensor (Sensor sensor){
		sense = sensor.getSense();
		size = 1;
		if(sensor.getExpr()!= null){
			r = sensor.getExpr();
			size += r.size();
		}
	}
	public Token getSense(){
		return sense;
	}
	public Expr getExpr() {
		return r;
	}
	public Sensor(Token s, Expr r){
		System.out.println(s.toString());
		if(s.isSensor()){
			this.sense = s;
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
		sb.append(sense.toString());
		if (r != null)
			sb.append("[" + r.toString() + "]");
		return sb;
	}

	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
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
		return (sense.getType().equals(TokenType.NEARBY) || sense.getType().equals(TokenType.AHEAD) || sense.getType().equals(TokenType.RANDOM));
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

	@Override
	public boolean sameType(Node n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Token getToken() {
		return getSense();
	}

	@Override
	public void setToken(Token t) {
		if (t.isSensor())
			sense = t;
	}
}