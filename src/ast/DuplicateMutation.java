package ast;

import java.util.ArrayList;
import java.util.Random;

public class DuplicateMutation implements Mutation {

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof DuplicateMutation);
	}
	
	public Node mutate(Node n) {
		if (n instanceof ListChildren) {
			ArrayList<Node> children = ((ListChildren) n).getChildren();
			Random r = new Random();
			int index = r.nextInt(children.size());
			children.add(children.get(index));
			((ListChildren) n).setChildren(children);
			return n;
		}
		else{
			return null;
		}
	}
	
	
}