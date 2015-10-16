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
			if (((ListChildren) n).getChildren().size()> 0){
				//System.out.println("This is true");
				ArrayList<Node> children = ((ListChildren) n).getChildren();
				Random r = new Random();
				int index = r.nextInt(children.size());
				return children.get(index);
			}
			return null;
		}
		else{
			return null;
		}
	}
	
	
}