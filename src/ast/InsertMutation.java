package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import parse.Token;
import parse.TokenType;
public class InsertMutation implements Mutation {

	Program p;
	
	@Override
	public boolean equals(Mutation m) {
		return (m instanceof InsertMutation);
	}
	
	public Node mutate(Node n) {
		if (n instanceof Expr) {
			Random r = new Random();
			int parity = r.nextInt(2);
			if (parity == 0){
				MemoryNode m = new MemoryNode((Expr) n);
				return m;
			}
			else {
				ArrayList<Expr> temp = new ArrayList<Expr>();
				for(int i = 0;i<p.size();i++){
					Node current = p.nodeAt(i);
					if (current instanceof Expr && !(current.equals(n)))
						temp.add((Expr) current);
				}
				Collections.shuffle(temp);
				Node copyTemplate =  temp.get(0);
				Node newCopy = CopyMutation.copy(n);
				int randomness = r.nextInt(5);
				TokenType t;
				switch(randomness){
				case 0:
					t = TokenType.getTypeFromString("+");
					break;
				case 1:
					t = TokenType.getTypeFromString("-");
					break;
				case 2:
					t = TokenType.getTypeFromString("*");
					break;
				case 3:
					t = TokenType.getTypeFromString("/");
					break;
				case 4:
					t = TokenType.getTypeFromString("mod");
					break;
				default:
					t = TokenType.getTypeFromString("+");
					break;
				}
				Token Operator = new Token(t, -1);
				return new BinaryOp((Expr) n,(Expr)newCopy,Operator);
			}
		}
		else if (n instanceof Condition) {
			Random r = new Random();
			ArrayList<Condition> temp = new ArrayList<Condition>();
			for(int i = 0;i<p.size();i++){
				Node current = p.nodeAt(i);
				if (current instanceof Condition && !(current.equals(n)))
					temp.add((Condition) current);
			}
			Collections.shuffle(temp);
			Node copyTemplate =  temp.get(0);
			Node newCopy = CopyMutation.copy(n);
			int randomness = r.nextInt(2);
			TokenType t;
			switch(randomness){
			case 0:
				t = TokenType.getTypeFromString("and");
				break;
			case 1:
				t = TokenType.getTypeFromString("or");
				break;
			default:
				t = TokenType.getTypeFromString("or");
				break;
			}
			Token Operator = new Token(t,-1);
			return new BinaryCondition((Condition) newCopy,Operator, (Condition) n);
		}
		else{
			return null;
		}
	}
}