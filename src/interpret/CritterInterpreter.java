package interpret;

import ast.Condition;
import ast.Expr;
import ast.Program;
import critter.Critter;
import world.World;

public class CritterInterpreter implements Interpreter {

	World w;
	Critter cr;
	
	@Override
	public Outcome interpret(Program p) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public  boolean eval(Condition c) {
		return c.evaluate(cr, w);
	}

	@Override
	public int eval(Expr e) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}