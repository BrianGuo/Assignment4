package ast;

import java.util.ArrayList;
public class Command implements Node {

	private ArrayList<UpdateNode> updates;
	private ActionNode action;
	int size;
	
	public Command() {
		updates = null;
		action = null;
	}
	public Command(ActionNode a){
		action = a;
	}
	
	public Command(ArrayList<UpdateNode> updates, ActionNode a){
		this.updates = updates;
		action = a;
		int temp = 0;
		for (int i = 0; i < updates.size(); i++){
			temp += updates.get(i).size();
		}
		temp += action.size;
		size = temp +1;
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		int temp = index;
		int updatesIndex = 0;
		while (updatesIndex < updates.size() && temp > updates.get(updatesIndex).size()){
			temp -= updates.get(updatesIndex).size();
			updatesIndex++;
		}
		return(updates.get(updatesIndex).nodeAt(temp - 1));
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		for(int i = 0; i< updates.size(); i++ ) {
			sb.append(updates.get(i).toString() + " ");
		}
		if (action != null)
			sb.append(action.toString());
		sb.append(";");
		return sb;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return prettyPrint(sb).toString();
	}
	
}