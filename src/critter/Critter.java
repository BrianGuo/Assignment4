package critter;


public class Critter{
	
	String species;
	int direction;
	int[] coordinates = new int[2];
	int[] attributes;
	
	public Critter(int[] attributes, int direction, String species, int[] coordinates) {
		this.attributes = attributes;
		this.direction = direction;
		this.species = species;
		this.coordinates = coordinates;
	}
	
	public int getAttributeAtIndex(int n){
		if (n < attributes.length)
			return attributes[n];
		else{
			System.out.println("there's no value at this index");
			return 0;
		}
	}
}