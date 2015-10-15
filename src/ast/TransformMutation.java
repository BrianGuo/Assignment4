package ast;

public class TransformMutation implements ParentSpecificMutation {

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof TransformMutation);
	}

	@Override
	public Node mutate(Node n) {
		
	}
	
	
}