package interpret;

import parse.TokenType;
import ast.Expr;
import critter.Critter;
public class CritterOutcome implements Outcome {
	
	Critter cr;
	TokenType action;
	Expr num;
	
	public CritterOutcome(Critter cr, TokenType action, Expr num){
		this.cr = cr;
		this.action = action;
		this.num = num;
	}
}