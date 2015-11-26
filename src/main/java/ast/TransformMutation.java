package ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import parse.Token;
import parse.TokenCategory;
import parse.TokenType;

public class TransformMutation implements ParentSpecificMutation {

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof TransformMutation);
	}

	@Override
	public Node mutate(Node n) {
		if (n instanceof Tokenable) {
			if (n instanceof ActionNode && (((Tokenable) n).getToken().getType().equals(TokenType.TAG)||((Tokenable) n).getToken().getType().equals(TokenType.SERVE))){
				Random r = new Random();
				int which = r.nextInt(2);
				if (which == 0){
					Token newToken = new Token(TokenType.TAG, -1);
					((Tokenable) n).setToken(newToken);
				}
				else {
					Token newToken = new Token(TokenType.SERVE,-1);
					((Tokenable) n).setToken(newToken);
				}
				return n;
			}
			else {
				Token t = ((Tokenable) n).getToken();
				TokenType t2 = t.getType();
				TokenCategory t3 = TokenType.getCategory(t2);
				ArrayList<TokenType> temp = TokenType.getAlloftype(t3);
				temp.remove(t2);
				temp.remove(TokenType.TAG);
				temp.remove(TokenType.SERVE);
				Collections.shuffle(temp);
				TokenType newTokenType = temp.get(0);
				Token newToken = new Token(newTokenType, -1);
				((Tokenable) n).setToken(newToken);
				return n;
			}
		}
		else if (n instanceof NumberNode){
			Random r = new Random();
			int den = r.nextInt();
			while (den == 0)
				den = r.nextInt();
			int num = Integer.MAX_VALUE;
			NumberNode newN;
			if (den%2 == 1)
				newN = new NumberNode(((NumberNode) n).getNum() + num/den);
			else
				newN = new NumberNode(((NumberNode) n).getNum() - num/den);
			return newN;	
		}
		else if (n instanceof UnaryNode && ((UnaryNode) n).hasChild()){
			Random r = new Random();
			int NewN = r.nextInt(5);
			switch(NewN){
			case 0:
				MemoryNode m = new MemoryNode((Expr) ((UnaryNode)n).getChild());
				return m;
			case 1:
				NegationNode m2 = new NegationNode((Expr) ((UnaryNode)n).getChild());
				return m2;
			case 2:
				Sensor m3 = new Sensor(new Token(TokenType.getTypeFromString("nearby"),-1), ((Expr) ((UnaryNode)n).getChild()));
				return m3;
			case 3:
				Sensor m4 = new Sensor(new Token(TokenType.getTypeFromString("ahead"),-1), ((Expr) ((UnaryNode)n).getChild()));
				return m4;
			case 4:
				Sensor m5 = new Sensor(new Token(TokenType.getTypeFromString("random"),-1), ((Expr) ((UnaryNode)n).getChild()));
				return m5;
			default:
				return null;
			}
		}
		else
			return null;
	}
	
	
}