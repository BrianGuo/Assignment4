package ast;

import java.util.ArrayList;
import java.util.Collections;

public class CopyMutation implements ParentSpecificMutation {

	Program p;
	
	public CopyMutation(Program p){
		this.p = p;
	}
	public CopyMutation(){
	}
	public static Node copy(Node n){
		if (n instanceof BinaryChildren){
			try{
			BinaryChildren BinaryN = (BinaryChildren) n.getClass().getDeclaredConstructor(n.getClass()).newInstance(n);
			BinaryN.setLeft(copy(BinaryN.getLeft()));
			BinaryN.setRight(copy(BinaryN.getRight()));
			return BinaryN;
			}
			catch(Exception e){
				System.out.println("ya dun goofed");
			}
		}
		else if (n instanceof ListChildren) {
			try{
				ListChildren ListN = (ListChildren) n.getClass().getDeclaredConstructor(n.getClass()).newInstance(n);
				ArrayList<Node> temp = new ArrayList<Node>();
				for (Node current: ListN.getChildren()){
					temp.add(copy(current));
				}
				ListN.setChildren(temp);
				return ListN;
			}
			catch(Exception e){
				System.out.println("ya dun goofed 2");
			}
		}
		else if (n instanceof UnaryNode){
			//System.out.println("reached here");
			try{
				UnaryNode UnaryN = (UnaryNode) n.getClass().getDeclaredConstructor(n.getClass()).newInstance(n);
				//System.out.println(UnaryN);
				if (((UnaryNode) n).hasChild())
					UnaryN.setChild(copy(UnaryN.getChild()));
				return UnaryN;
			}
			catch(Exception e) {
				System.out.println("ya dun goofed 3");
			}
		}
		return null;
	}
	public Node mutate(Node n) {
		//System.out.println("reached this point");
		int size = p.size();
		int current = 0;
		ArrayList<Node> candidates = new ArrayList<Node>();
		while(current < size){
			if (p.nodeAt(current).sameType(n) && !(p.nodeAt(current).equals(n))){
				candidates.add(p.nodeAt(current));
			}
			current ++;
		}
		if (candidates.size() > 0){
			Collections.shuffle(candidates);
			return copy(candidates.get(0));
		}
		return null;
	}
	
	@Override
	public boolean equals(Mutation m) {
		return (m instanceof CopyMutation);
	}

	
	
	public void setProgram(Program p){
		this.p = p;
	}
	
}