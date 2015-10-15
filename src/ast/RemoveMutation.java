package ast;

import java.util.ArrayList;
import java.util.Collections;

public class RemoveMutation implements ParentSpecificMutation {

	Program p;
	
	public void setProgram(Program p){
		this.p = p;
	}
	public Node mutate(Node n){
		ArrayList<Node> candidates = new ArrayList<Node>();
		for(int i = 0; i < n.size(); i++ ){
			if (n.sameType(n.nodeAt(i)))
				candidates.add(n.nodeAt(i));
		}
		Collections.shuffle(candidates);
		return candidates.get(0);
		return null;
	}
	
	@Override
	public boolean equals(Mutation m) {
		return (m instanceof RemoveMutation);
	}
	
	
}