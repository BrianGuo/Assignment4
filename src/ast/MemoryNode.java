package ast;

import java.util.ArrayList;

import critter.Critter;
import parse.Token;
import parse.TokenType;
import world.World;

/**
 * Represents a memory access, such as mem[5].  Also handles syntactic sugar for memory locations.
 * {@code expression} The index of memory to access
 */
public class MemoryNode extends UnaryNode implements Expr {

	private Token MemoryToken;
	private Expr expression;
	//private int location;
	//private int size;
	
	
	public MemoryNode(Expr exp){
		expression = exp;
		//size = expression.size()+1;
		generateSugar();
	}
	public void generateSugar() {
		if (expression == null)
			return;
		else if (!(expression instanceof NumberNode))
			return;
		else{
			NumberNode expr = (NumberNode) expression;
			switch(expr.getNum()){
			case 0:
				MemoryToken = new Token(TokenType.getTypeFromString("MEMSIZE"),-1);
				break;
			case 1:
				MemoryToken = new Token(TokenType.getTypeFromString("DEFENSE"),-1);
				break;
			case 2:
				MemoryToken = new Token(TokenType.getTypeFromString("OFFENSE"),-1);
				break;
			case 3:
				MemoryToken = new Token(TokenType.getTypeFromString("SIZE"),-1);
				break;
			case 4:
				MemoryToken = new Token(TokenType.getTypeFromString("ENERGY"),-1);
				break;
			case 5:
				MemoryToken = new Token(TokenType.getTypeFromString("PASS"),-1);
				break;
			case 6:
				MemoryToken = new Token(TokenType.getTypeFromString("TAG"),-1);
				break;
			case 7:
				MemoryToken = new Token(TokenType.getTypeFromString("POSTURE"),-1);
				break;
			default:
				break;
			}
		}
	}
	public MemoryNode(Expr exp, Token T){
		System.out.println("Token in MemoryNode constructor:" + T);
		expression = exp;
		if (T.isMemSugar())
			MemoryToken = T;
		//size = exp.size()+1;
	}
	
	public MemoryNode(MemoryNode m) {
		if (m.getExpression() != null){
			expression = m.getExpression();
			generateSugar();
			//size = expression.size()+1;
		}
	}
	
	public Token getMemoryToken(){
		return MemoryToken;
	}
	public Expr getExpression() {
		return expression;
	}
	@Override
	public int size() {
		int size = 1 + expression.size();
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index >= size() || index < 0)
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
		if (MemoryToken != null)
			return sb.append(MemoryToken.toString());
		else
			return sb.append("mem[" + expression.toString() + "]");
	}
	
	public String toString() {
		StringBuilder temp = new StringBuilder();
		return prettyPrint(temp).toString();
	}

	/*@Override
	public int evaluate() {
		return 0;
	}*/

	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		if (expression != null)
			temp.add(expression);
		return temp;
	}

	
	@Override
	public boolean hasChild(){
		return (expression != null);
	}
	
	@Override
	public void setChild(Node n) {
		if (n instanceof Expr){
			expression = (Expr) n;
			//size = n.size() + 1;
		}
	}
	
	@Override
	public Node getChild() {
		return expression;
	}
	
	@Override
	public int evaluate(Critter c, World w) {
		int n = expression.evaluate(c, w);
		return c.getAttributeAtIndex(n);
	}
	
}