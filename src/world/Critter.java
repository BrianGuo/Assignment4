package world;

import java.util.Random;

import ast.Program;
import ast.Rule;

public class Critter extends Entity{
	
	Program p;
	String species;
	int direction;
	//Coordinate location;
	int[] attributes;
	Rule LastRule;
	//Coordinate coordinates;
	

	public Critter(int[] attributes, int direction, String species, Coordinate coordinates, WorldConstants constants,
				   Program program) {
		assert(attributes[0] >= 8);
		this.attributes = attributes;
		this.direction = direction;
		this.species = species;
		this.location = coordinates;
		p = program;

		//if memsize is greater than 8, we need to add slots for the extra memory locations
		if(attributes.length < attributes[0]){
			int[] temp = new int[attributes[0]];
			System.arraycopy(attributes, 0, temp, 0, attributes.length);
			this.attributes = temp;
		}
		else{
			this.attributes = attributes;
		}


	}

	public int getAttributeAtIndex(int n){
		if (n < attributes.length)
			return attributes[n];
		else{
			System.out.println("there's no value at this index");
			return 0;
		}
	}
	public String getLastRule() {
		if (LastRule != null)
			return LastRule.toString();
		else
			return null;
	}
	
	public void setLastRule(Rule r) {
		LastRule = r;
	}
	public String getSpecies() {
		return species;
	}
	public Program getProgram() {
		return p;
	}
	public int getDirection() {
		return direction;
	}
	/*
	public Coordinate getLocation() {
		return location;
	}*/
	public void UpdateNodeAt(int index, int value){
		if (index < 7 || index > attributes[0])
			return;
		else if (index == 7 && (value < 0 || value > 99))
			return;
		else
			attributes[index] = value;
	}
	
	public void setNodeAt(int index, int value) {
		attributes[index] = value;
	}

	public String toString(){
		//TODO
		return "";
	}

	@Override
	public int appearance() {
		if (attributes != null && attributes.length >= 8) {
			return attributes[3] * 100000 + attributes[6] + 1000 + attributes[7] * 10 + direction;
		}
		else{
			System.out.println("Something's wrong with your critter, the attributes are not valid");
			return 0;
		}
	}
	private void mutateAttr() {
		Random r = new Random();
		int i = r.nextInt(3);
		switch(i) {
		case 0:
			if (r.nextBoolean()){
				attributes[0]++;
				if(attributes.length < attributes[0]){
					int[] temp = new int[attributes[0]];
					System.arraycopy(attributes, 0, temp, 0, attributes.length);
					this.attributes = temp;
				}
			}
			else if(attributes[0] > 8){
				attributes[0]--;
			}
			break;
		case 1:
			if (r.nextBoolean())
				attributes[1]++;
			else if (attributes[1] > 1)
				attributes[1]--;
			break;
		case 2:
			if (r.nextBoolean())
				attributes[2]++;
			else if (attributes[2]>1)
				attributes[2]--;
			break;
		default:
			break;
		}
	}
	public void mutate() {
		Random r = new Random();
		int counter = 1;
		while(r.nextInt((int) Math.pow(4, counter)) == 0){
			if (r.nextBoolean())
				mutateAttr();
			else
				p.mutate();
		}
	}
	
	public void setDirection (int n) {
		if (0<= n  && n<= 5) {
			direction = n;
		}
	}

	public int size(){
		return getAttributeAtIndex(3);
	}
}