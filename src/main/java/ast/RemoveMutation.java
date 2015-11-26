package ast;

import java.util.ArrayList;
import java.util.Collections;

public class RemoveMutation implements ParentSpecificMutation {

	Program p;
	
	public void setProgram(Program p){
		this.p = p;
	}
	public Node mutate(Node n){
		if(n instanceof UnaryNode && ((UnaryNode) n).hasChild() || n instanceof BinaryCondition || n instanceof BinaryOp){
			ArrayList<Node> candidates = new ArrayList<Node>();
			for(int i = 1; i < n.size(); i++ ){
				candidates.add(n.nodeAt(i));
			}
			Collections.shuffle(candidates);
			if (candidates.size()>0)
				return candidates.get(0);
			return null;
		}
		else{
			return null;
		}
	}
	
	@Override
	public boolean equals(Mutation m) {
		return (m instanceof RemoveMutation);
	}
	
	
}