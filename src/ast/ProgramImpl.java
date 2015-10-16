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
			System.out.println("nodeAt " + index + ": " + rules.get(currentRule).nodeAt(temp-1));
        	return rules.get(currentRule).nodeAt(temp-1);
        }
    }

    @Override
    public Program mutate() {
        Random r = new Random();
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
    	return null; //shouldn't happen?
    }

    @Override
    public Program mutate(int index, Mutation m) {
        Node n = nodeAt(index);
        System.out.println("A " + m.getClass() + " mutation is performed on Node #" + index + " which is a " + n.getClass() + " node that represents the text (" + n + ")");
        boolean worked = false;
        Node parent = findParent(index);
        if(m instanceof RemoveMutation){
        	if (n instanceof Rule || n instanceof UpdateNode){
        		if (((ListChildren) parent).getChildren().size() > 1){
        			ArrayList<Node> children = ((ListChildren) parent).getChildren();
        			worked = children.remove(n);
        			((ListChildren) parent).setChildren(children);
        		}
        	}
        	else{
	        	((RemoveMutation) m).setProgram(this);
	        	Node mutated = ((RemoveMutation) m).mutate(n);
	        	System.out.println("If this works, the new Node will be " + mutated);
	        	worked = addChild(parent,n,mutated);

        	}
        }
        else if (m instanceof SwapMutation) {
        	worked = ((SwapMutation) m).mutate(n);
        }
        else if (m instanceof CopyMutation){
        	((CopyMutation) m).setProgram(this);
        	Node copy = ((CopyMutation) m).mutate(n);
        	System.out.println("If this works, the new Node will be " + copy);
        	if (n instanceof Rule || n instanceof UpdateNode) {
        		ArrayList<Node> children = ((ListChildren) parent).children();
        		int index2 = children.indexOf(n);
        		if (index2 != -1){
        			children.set(index2, copy);
        			((ListChildren) parent).setChildren(children);
        			worked = true;
        		}
        	}
        	else
        		worked = addChild(parent, n, copy);
        }
        else if (m instanceof TransformMutation) {
        	Node transformed = ((TransformMutation) m).mutate(n);
        	System.out.println("If this works, the new Node will be " + transformed);
        	worked = addChild(parent,n,transformed);
        }
        else if (m instanceof InsertMutation){
        	((InsertMutation) m).setProgram(this);
        	Node insertNode = ((InsertMutation) m).mutate(n);
        	System.out.println("If this works, the new Node will be " + insertNode);
        	worked = addChild(parent, n, insertNode);
        }
        else if (m instanceof DuplicateMutation){
        	Node duplicateNode = ((DuplicateMutation) m).mutate(n);
        	System.out.println("If this works, the new Node will be " + duplicateNode);
        	worked = addChild(n,n,duplicateNode);
        }
        if (worked)
        	System.out.println("It was a success.");
        else
        	System.out.println("It didn't work");
        return this;
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
			System.out.println("final temp:" + temp);
			System.out.println("final current:" + current);
			System.out.println("m:" + m);
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
