package interpret;

import java.util.ArrayList;


import ast.Command;
import ast.Condition;
import ast.Expr;
import ast.Program;
import ast.Rule;
import ast.UpdateNode;
import world.Critter;
import parse.TokenType;
import world.World;

public class CritterInterpreter implements Interpreter {

	World w;
	Critter cr;
	
	@Override
	public Outcome interpret(Program p) {
		assert(cr.getProgram().equals(p));
		ArrayList<Rule> rules = p.getRules();
		int current = 0;
		cr.setNodeAt(5, 1);
		while (current < rules.size()){
			if (cr.getAttributeAtIndex(5) > 999) {
				return new CritterOutcome(cr,TokenType.WAIT,null);
			}
			Rule r = rules.get(current);
			if (((Condition)r.getLeft()).evaluate(cr, w)){
				Command com = (Command) r.getRight();
				ArrayList<UpdateNode> updates = com.getUpdates();
				if (updates != null){
					for (UpdateNode u: updates){
						perform(u);
					}
				}
				if (com.getAction() != null){
					cr.setLastRule(r);
					return new CritterOutcome(cr,com.getAction().getToken().getType(),com.getAction().getNum());
				}
				else{
					cr.setNodeAt(5, cr.getAttributeAtIndex(5)+1);
				}
				current = 0;
			}
			else
				current++;
		}
		return new CritterOutcome(cr, TokenType.WAIT, null);
	}
	@Override
	public void perform(UpdateNode u){
		int left = u.getLeft().evaluate(cr, w);
		int right = u.getRight().evaluate(cr, w);
		cr.UpdateNodeAt(left,right);
	}
	
	@Override
	/**
	 * Evaluates the condition c
	 * @Precondition: cr and w are set to valid non-null values
	 * @param e  The condition to be evaluated
	 * @returns boolean representing the value of c
	 */
	public  boolean eval(Condition c) {
		return c.evaluate(cr, w);
	}

	@Override
	/**
	 * Evaluates the expression e
	 * @Precondition: cr and w are set to valid non-null values
	 * @param e  The expression to be evaluated
	 * @returns integer representing the value of e
	 */
	public int eval(Expr e) {
		return e.evaluate(cr, w);
	}
	
	public void setWorld(World w) {
		this.w = w;
	}
	
	public void setCritter(Critter c) {
		this.cr = c;
	}
	
}