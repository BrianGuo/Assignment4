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
		while (current < rules.size()){
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
					return new CritterOutcome(cr,com.getAction().getToken().getType(),com.getAction().getNum());
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
	public  boolean eval(Condition c) {
		return c.evaluate(cr, w);
	}

	@Override
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