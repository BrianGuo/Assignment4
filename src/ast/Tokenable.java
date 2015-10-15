package ast;

import parse.Token;

public interface Tokenable {
	
	Token getToken();
	
	void setToken(Token t);
}