package ast;

import java.util.ArrayList;
public class Command extends ListChildren implements Node {

	private ArrayList<UpdateNode> updates;
	private ActionNode action;
	//private int size;
	
	public Command() {
		updates = new ArrayList<>();
		//size = 1;
		action = null;
	}
	
	public Command(Command c){
		this.updates = c.getUpdates();
		this.action = c.getAction();
		//this.size = c.size();
	}
	
	public Command(ActionNode a){
		action = a;
		//size = a.size();
	}

	public Command(ArrayList<UpdateNode> updates){
		action = null;
		this.updates = updates;
		//size = 1;
		//for(UpdateNode u: updates)
			//size += u.size();
	}
	public Command(ArrayList<UpdateNode> updates, ActionNode a){
		this.updates = updates;
		action = a;
		/*int temp = 0;
		for (int i = 0; i < updates.size(); i++){
			temp += updates.get(i).size();
		}
		temp += action.size;
		size = temp +1;*/
	}
	public ArrayList<UpdateNode> getUpdates(){
		return updates;
	}
	public ActionNode getAction(){
		return action;
	}
	@Override
	public int size() {
		int size = 1;
		if (action != null)
			size += action.size();
		if (updates != null){
			for (UpdateNode u: updates)
				size += u.size();
		}
		return size;
	}

	@Override
	public Node nodeAt(int index) {
		if (index == 0)
			return this;
		else if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		int temp = index;
		int updatesIndex = 0;
		while (updatesIndex < updates.size() && temp > updates.get(updatesIndex).size()){
			temp -= updates.get(updatesIndex).size();
			updatesIndex++;
		}
		if(updatesIndex == updates.size())
			return action.nodeAt(temp - 1);
		else
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
	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		for (UpdateNode u: updates)
			temp.add(u);
		if (action != null)
			temp.add(action);
		return temp;
	}
	
	@Override
	public ArrayList<Node> getChildren() {
		ArrayList<Node> result = new ArrayList<Node>();
		for(UpdateNode u: updates)
			result.add(u);
		return result;
	}
	
	@Override
	public boolean sameType(Node n) {
		return (n instanceof Command);
	}
	@Override
	public void setChildren(ArrayList<Node> n) {
		ArrayList<UpdateNode> temp = new ArrayList<UpdateNode>();
		//size = 1;
		//if (action != null)
			//size += action.size();
		for(Node instance: n){
			//size += instance.size();
			temp.add((UpdateNode) instance);
		}
		updates = temp;
	}
	
	public void addUpdate(UpdateNode u) {
		updates.add(u);
		//size += u.size();
	}
	public void setAction(ActionNode a){action = a;}
	
}