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
			Token t = ((Tokenable) n).getToken();
			TokenType t2 = t.getType();
			TokenCategory t3 = TokenType.getCategory(t2);
			ArrayList<TokenType> temp = TokenType.getAlloftype(t3);
			temp.remove(t2);
			Collections.shuffle(temp);
			TokenType newTokenType = temp.get(0);
			Token newToken = new Token(newTokenType, -1);
			((Tokenable) n).setToken(newToken);
			return n;
		}
		else if (n instanceof NumberNode){
			Random r = new Random();
			int den = r.nextInt();
			int num = Integer.MAX_VALUE;
			NumberNode newN;
			if (den%2 == 1)
				newN = new NumberNode(((NumberNode) n).getNum() + num/den);
			else
				newN = new NumberNode(((NumberNode) n).getNum() - num/den);
			return newN;	
		}
		else
			return null;
	}
	
	
}