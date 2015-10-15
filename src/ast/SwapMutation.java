package ast;

import java.util.ArrayList;

public class SwapMutation implements Mutation {

	@Override
	public boolean equals(Mutation m) {
		return (m instanceof SwapMutation);
	}
	
	public boolean mutate(Node n) {
		ArrayList<Node> temp = n.children();
		if (temp.size() < 2)
			return false;
		else{
			if (n instanceof BinaryChildren && !(n instanceof Rule)){
				BinaryChildren BinaryN = (BinaryChildren) n;
				Node temp2 = BinaryN.getLeft();
				BinaryN.setLeft(BinaryN.getRight());
				BinaryN.setRight(temp2);
				return true;
			}
			else if (n instanceof ListChildren){
				ListChildren ListN = (ListChildren) n;
				ArrayList<? extends Node> children = ListN.getChildren();
				ArrayList<Node> children2 = new ArrayList<Node>();
				children2.add(children.get(1));
				children2.add(children.get(0));
				children2.addAll(2, children);
				ListN.setChildren(children2);
				return true;
			}
			else{
				return false;
			}
		}
	}
	
}