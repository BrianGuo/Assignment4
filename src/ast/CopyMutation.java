package ast;

import java.util.ArrayList;
import java.util.Arrays;

public class CopyMutation implements ParentSpecificMutation {

	Program p;
	
	public CopyMutation(Program p){
		this.p = p;
	}
	public CopyMutation(){
	}
	public Node copy(Node n){
		if (n instanceof BinaryChildren){
			try{
			BinaryChildren BinaryN = (BinaryChildren) n.getClass().getDeclaredConstructor(n.getClass()).newInstance(n);
			BinaryN.setLeft(copy(BinaryN.getLeft()));
			BinaryN.setRight(copy(BinaryN.getRight()));
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
			}
			catch(Exception e){
				System.out.println("ya dun goofed 2");
			}
		}
		return null;
			
			
	}
	public Node mutate(Node n) {
		int size = p.size();
		int current = 0;
		while(current < size){
			if (p.nodeAt(current).sameType(n) && !(p.nodeAt(current).equals(n)))
				return p.nodeAt(current);
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