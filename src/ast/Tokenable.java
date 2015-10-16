package ast;

import parse.Token;

/**
 * For private use with the transform mutation.
 */
public interface Tokenable {
	
	Token getToken();
	
	void setToken(Token t);
}