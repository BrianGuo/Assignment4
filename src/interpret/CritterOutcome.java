package interpret;

import parse.TokenType;
import ast.Expr;
import world.Critter;
public class CritterOutcome implements Outcome {
	
	Critter cr;
	TokenType action;
	Expr num;
	
	public CritterOutcome(Critter cr, TokenType action, Expr num){
		this.cr = cr;
		this.action = action;
		this.num = num;
	}

	public Critter getCritter(){
		return cr;
	}
	//public

}