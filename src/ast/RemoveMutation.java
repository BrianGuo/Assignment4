package ast;

import java.util.ArrayList;

public class RemoveMutation implements ParentSpecificMutation {

	
	public Node mutate(Node n){
		ArrayList<Node> candidates = n.children();
		for (Node possible: candidates){
			if (n.sameType(possible))
				return n;
		}
		return null;
	}
	
	@Override
	public boolean equals(Mutation m) {
		return (m instanceof RemoveMutation);
	}
	
	
}