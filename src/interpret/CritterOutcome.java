package interpret;

import parse.TokenType;
import ast.Expr;
import world.Critter;
public class CritterOutcome implements Outcome {

	Critter cr;
	TokenType action;
	Expr num;

	/**
	 * Creates an Outcome with given fields
	 * Requires: action not null
	 * Requires: critter not null
	 * @param cr   the critter to perform this action on
	 * @param action    the action to be performed
	 * @param num     the possible number associated with the action
	 */
	public CritterOutcome(Critter cr, TokenType action, Expr num){
		this.cr = cr;
		this.action = action;
		this.num = num;
	}

	/**
	 * Returns the critter field
	 */
	public Critter getCritter(){
		return cr;
	}
	
	/**
	 * Returns the action field
	 */
	public TokenType getAction() { return action; }

	/**
	 * Returns the number field
	 */
	public Expr getExpr() { return num; }
}