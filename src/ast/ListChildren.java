package ast;

import java.util.ArrayList;

public abstract class ListChildren {

	public ListChildren(){};
	public ListChildren(ListChildren l){
		
	}
	public ArrayList<? extends Node> getChildren(){return null;}
	public void setChildren(ArrayList<Node> n){}
}