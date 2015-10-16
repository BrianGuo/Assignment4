package ast;

import java.util.ArrayList;
import java.util.Random;

/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl extends ListChildren implements Program {

    private ArrayList<Rule> rules;
    //private int size;

    
    public ProgramImpl(){
    	rules = new ArrayList<Rule>();
    	//size = 0;
    }
    public ProgramImpl(ArrayList<Rule> r){
    	rules = r;
    	//size = 0;
    	//for(Rule i: r){
    		//size += i.size();
    	//}
    	//size ++;
    }
    
    public ProgramImpl(ProgramImpl p){
    	
    	this.rules = p.getRules();
    }
    
    public ArrayList<Rule> getRules(){
    	return rules;
    }
    public int size() {
    	int size = 1;
    	for (Rule r: rules)
    		size += r.size();
        return size;
    }

    public boolean addRule(Rule r) {
    	rules.add(r);
    	//size += r.size();
    	return true;
    }
    
    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index >= size())
        	throw new IndexOutOfBoundsException();
        else{
        	int temp = index;
        	int currentRule = 0;
        	while(temp > rules.get(currentRule).size() && currentRule < rules.size()){
        		temp -= rules.get(currentRule).size();
        		currentRule++;
        	}
        	return rules.get(currentRule).nodeAt(temp-1);
        }
    }

    @Override
    public Program mutate() {
        Random r = new Random();
        int AttrOrRule = r.nextInt(2);
        if (AttrOrRule == 0)
        	return this;
        else{
        	System.out.println("Reached Here");
        	int selected = r.nextInt(size());
        	int whichMut = r.nextInt(6);
        	Mutation m;
        	switch(whichMut){
        	case 0:
        		m = MutationFactory.getRemove();
        		if (nodeAt(selected) instanceof Rule || nodeAt(selected) instanceof UpdateNode){
        			if(findParent(selected).children().size() == 1)
        				return this;
        			else{
        				ArrayList<Node> temp = findParent(selected).children();
        				temp.remove(nodeAt(selected));
        				((ListChildren) findParent(selected)).setChildren(temp);
        			}
        		}
        		return mutate(selected, m);
        	case 1:
        		m = MutationFactory.getSwap();
        		return mutate(selected,m);
        	case 2:
        		m = MutationFactory.getReplace();
        		return mutate(selected,m);
        	case 3:
        		m = MutationFactory.getTransform();
        		return mutate(selected,m);
        	case 4:
        		m = MutationFactory.getInsert();
        		return mutate(selected,m);
        	case 5:
        		m = MutationFactory.getDuplicate();
        		return mutate(selected,m);
        	}
        }
		return null; //shouldn't happen?
    }

    @Override
    public Program mutate(int index, Mutation m) {
        Node n = nodeAt(index);
        Node parent = findParent(index);
        if(m instanceof RemoveMutation){
        	if (n instanceof Rule || n instanceof UpdateNode){
        		if (((ListChildren) parent).getChildren().size() > 1){
        			ArrayList<Node> children = ((ListChildren) parent).getChildren();
        			System.out.println(children.remove(n));
        			((ListChildren) parent).setChildren(children);
        			return this;
        		}
        		else
        			return this;
        	}
        	else{
	        	((RemoveMutation) m).setProgram(this);
	        	Node mutated = ((RemoveMutation) m).mutate(n);
	        	System.out.println(addChild(parent,n,mutated));
	        	return this;
        	}
        }
        else if (m instanceof SwapMutation) {
        	((SwapMutation) m).mutate(n);
        	return this;
        }
        else if (m instanceof CopyMutation){
        	((CopyMutation) m).setProgram(this);
        	Node copy = ((CopyMutation) m).mutate(n);
        	System.out.println(addChild(parent, n, copy));
        	return this;
        }
        else if (m instanceof TransformMutation) {
        	Node transformed = ((TransformMutation) m).mutate(n);
        	System.out.println(addChild(parent,n,transformed));
        	return this;
        }
        else if (m instanceof InsertMutation){
        	((InsertMutation) m).setProgram(this);
        	Node insertNode = ((InsertMutation) m).mutate(n);
        	System.out.println(addChild(parent, n, insertNode));
        	return this;
        }
        else if (m instanceof DuplicateMutation){
        	Node duplicateNode = ((DuplicateMutation) m).mutate(n);
        	addChild(n,n,duplicateNode);
        	return this;
        }
        else{
        	return this;
        }
    }

    public boolean addChild(Node parent, Node original, Node newNode) {
    	if(newNode == null){
    		System.out.println("Line 151");
    		return false;
    	}
    	if (parent instanceof UnaryNode) {
    		if (((UnaryNode) parent).hasChild()){
    			((UnaryNode) parent).setChild(newNode);
    			return true;
    		}
    		System.out.println("Line 159");
    		return false;
    	}
    	else if (parent instanceof BinaryChildren) {
    		if (((BinaryChildren) parent).getLeft().equals(original)){
    			((BinaryChildren) parent).setLeft(newNode);
    			return true;
    		}
    		else if (((BinaryChildren) parent).getRight().equals(original)){
    			((BinaryChildren) parent).setRight(newNode);
    			return true;
    		}
    		else{
    			System.out.println("Line 172");
    			return false;
    		}
    	}
    	else if (parent instanceof ListChildren) {
    		ArrayList<Node> children = ((ListChildren) parent).getChildren();
    		children.add(newNode);
    		((ListChildren) parent).setChildren(children);
    		return true;
    	}
    	System.out.println("line 180");
    	return false;
    }
    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        for(Rule r: rules){
        	sb.append(r.toString() + "\n");
        }
        return sb;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return prettyPrint(sb).toString();
    }
    
    @Override 
    public ArrayList<Node> getChildren(){
    	return children();
    }
    
	@Override
	public ArrayList<Node> children() {
		ArrayList<Node> temp = new ArrayList<Node>();
		for (Rule r: rules)
			if (r != null)
				temp.add(r);
		return temp;
		
	}
	@Override
	public boolean sameType(Node n) {
		return (n instanceof ProgramImpl);
	}
	@Override
	public Node findParent (int n) {
		if (n>= size() || n<= 0)
			return null;
		int index = n;
		Node m = nodeAt(n);
		Node result = this;
		ArrayList<Node> temp = children();
		while(!(temp.contains(m))){
			int current = 0;
			while(index > temp.get(current).size()){
				index -= temp.get(current).size();
				current ++;
			}
			result = temp.get(current);
			//System.out.println(result.getClass());
			temp = temp.get(current).children();
			current = 0;
			index --;
			
		}
		return result;
	}
	@Override
	public void setChildren(ArrayList<Node> n) {
		ArrayList<Rule> temp = new ArrayList<Rule>();
		//size = 1;
		for (Node instance: n){
			//size += instance.size();
			temp.add((Rule) instance);
		}
		rules = temp;
	}
	

}
