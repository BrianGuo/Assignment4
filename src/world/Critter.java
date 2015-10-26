package world;

import java.util.Random;

import ast.Program;
import world.Entity;

public class Critter extends Entity{
	
	Program p;
	String species;
	int direction;
	int[] coordinates = new int[2];
	int[] attributes;
	int memsize;
	
	public Critter(int[] attributes, int direction, String species, int[] coordinates, int memsize) {
		this.attributes = attributes;
		this.direction = direction;
		this.species = species;
		this.coordinates = coordinates;
		this.memsize = memsize;
	}
	
	public int getAttributeAtIndex(int n){
		if (n < attributes.length)
			return attributes[n];
		else{
			System.out.println("there's no value at this index");
			return 0;
		}
	}
	
	public Program getProgram() {
		return p;
	}
	public int getDirection() {
		return direction;
	}
	public int[] getCoordinates() {
		return coordinates;
	}
	public void UpdateNodeAt(int index, int value){
		if (index < 7 || index > memsize)
			return;
		else if (index == 7 && (value < 0 || value > 99))
			return;
		else
			attributes[index] = value;
	}

	public String toString(){
		//TODO
		return "";
	}

	@Override
	public int appearance() {

		//TODO
		return 0;
	}
	private void mutateAttr() {
		Random r = new Random();
		int i = r.nextInt(3);
		switch(i) {
		case 0:
			if (r.nextBoolean())
				attributes[0]++;
			else if(attributes[0] > 8)
				attributes[0]--;
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
}