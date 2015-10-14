package ast;

import java.util.ArrayList;

/**
 * A data structure representing a critter program.
 *
 */
public class ProgramImpl implements Program {

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
    public int size() {
        return size;
    }

    @Override
    public Node nodeAt(int index) {
        if (index == 0)
        	return this;
        else if (index < 0 || index > size)
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
        // TODO Auto-generated method stub
        return null;
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

}
