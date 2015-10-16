package ast;

import java.util.ArrayList;
import java.util.Random;

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
				Random r = new Random();
				ListChildren ListN = (ListChildren) n;
				ArrayList<Node> children = ListN.getChildren();
				if (children.size() < 2)
					return false;
				int index1 = r.nextInt(children.size());
				int index2 = r.nextInt(children.size());
				while(index2 == index1)
					index2 = r.nextInt(children.size());
				Node temp2 = children.get(index1);
				children.set(index1, children.get(index2));
				children.set(index2, temp2);
				ListN.setChildren(children);
				return true;
			}
			else{
				return false;
			}
		}
	}
	
}