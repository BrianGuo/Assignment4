package ast;

import java.util.ArrayList;

import ast.BinaryOp.Operation;

public class driver {

	
	public static void main(String[] args){
		NumberNode n = new NumberNode(5);
		NumberNode m = new NumberNode(6);
		BinaryOp b = new BinaryOp(n,m,Operation.PLUS);
		System.out.println(b.getLeft());
		System.out.println(b.getRight());
		MutationFactory m2 = new MutationFactory();
		CopyMutation mut = (CopyMutation) m2.getReplace();
		Node temp = mut.copy(b);
		/*mut.mutate(b);
		System.out.println(b.getLeft());
		System.out.println(b.getRight());
		ArrayList<Rule> a = new ArrayList<Rule>();
		ArrayList<Node> b = (ArrayList<Node>) a;*/
		
		
	}
}