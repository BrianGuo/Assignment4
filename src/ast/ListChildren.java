package ast;

import java.util.ArrayList;

/**
 * Represents any Node which can store a variable number of children, such as ProgramImpl (rules) and Commands (updates).
 */

public abstract class ListChildren implements Node {

	public ListChildren(){};
	public ListChildren(ListChildren l){
		
	}
	public ArrayList<Node> getChildren(){return null;}
	public void setChildren(ArrayList<Node> n){}
}