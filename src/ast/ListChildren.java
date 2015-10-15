package ast;

import java.util.ArrayList;

public abstract class ListChildren implements Node {

	public ListChildren(){};
	public ListChildren(ListChildren l){
		
	}
	public ArrayList<Node> getChildren(){return null;}
	public void setChildren(ArrayList<Node> n){}
}