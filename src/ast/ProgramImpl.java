package ast;

import java.util.ArrayList;
import java.util.Random;

/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl extends ListChildren implements Program {

    private ArrayList<Rule> rules;
    private int size;

    
    public ProgramImpl(){
    	rules = new ArrayList<Rule>();
    	size = 0;
    }
    public ProgramImpl(ArrayList<Rule> r){
    	rules = r;
    	size = 0;
    	for(Rule i: r){
    		size += i.size();
    	}
    	size ++;
    }
    
    public ProgramImpl(ProgramImpl p){
    	
    	this.rules = p.getRules();
    }
    
    public ArrayList<Rule> getRules(){
    	return rules;
    }
    public int size() {
        return size;
    }

    public boolean addRule(Rule r) {
    	rules.add(r);
    	return true;
    }
    
    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index >= size)
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
        // TODO Auto-generated method stub
        return null;
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
		if (n>= size || n<= 0)
			return null;
		int index = n;
		Node m = nodeAt(n);
		Node result = null;
		ArrayList<Node> temp = children();
		while(!(temp.contains(m))){
			int current = 0;
			while(index > temp.get(current).size()){
				index -= temp.get(current).size();
				current ++;
			}
			result = temp.get(current);
			temp = temp.get(current).children();
			current = 0;
		}
		return result;
	}
	@Override
	public void setChildren(ArrayList<Node> n) {
		ArrayList<Rule> temp = new ArrayList<Rule>();
		for (Node instance: n)
			temp.add((Rule) instance);
		rules = temp;
	}
	

}
