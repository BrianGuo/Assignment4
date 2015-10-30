package world;

import java.util.Random;

import ast.Program;
import ast.Rule;
import parse.TokenType;

/**
 * attributes[4] <= attributes[3] * ENERGY_PER_SIZE
 */
public class Critter extends Entity{
	
	Program p;
	String species;
	int direction;
	int[] attributes;
	Rule LastRule;
	boolean isDead = false;


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
		return "" + direction;
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

	/**
	 * Sets direction to the given direction.
	 * If the direction is not in the correct range of [0,5], take modulo so that it is.
	 * setDirection(-1) is equivalent to setDirection(5).
	 * @param n new direction
	 */
	public void setDirection (int n) {
		direction = ((n % 6) + 6) % 6;
	}

	/**
	 * Turns the critter one step either left or right
	 * If, somehow, another TokenType is fed into the critter (??), nothing happens but energy is consumed.
	 * @param turnDirection either LEFT to turn left or RIGHT to turn right.
	 */
	public void turn(TokenType turnDirection){
		//no need for dead-checking because this action only lowers energy, and
		//has no side effects that need to be considered midway.
		consumeEnergy(size());
		if (turnDirection == TokenType.LEFT){
			setDirection(direction-1);
		}
		else if (turnDirection == TokenType.RIGHT){
			setDirection(direction+1);
		}
	}

	/**
	 * Requires: amt > 0
	 * Lowers the critter's energy by {@code amt}.  If this would put the critter at 0 or below energy,
	 * sets isConsumed to true.
	 * @param amt
	 */
	private void consumeEnergy(int amt){
		attributes[4] -= amt;
		if(attributes[4] <= 0){
			isDead = true;
		}
	}

	/**
	 * The logistic function, for use with attacking and defending.
	 * @param x x.
	 * @return P(x).
	 */
	private double logistic (double x){
		return 1 / (1 + Math.pow(Math.E, -x));
	}

	public int size(){
		return getAttributeAtIndex(3);
	}

	public boolean isDead(){
		return isDead;
	}

	/**
	 * Calculates the complexity of a critter, as given by
	 * complexity = r * RULE_COST + (offense + defense) * ABILITY_COST
	 * @return complexity of the critter
	 */
	public int complexity(){
		return p.getRules().size() * constants.RULE_COST + (attributes[1] + attributes[2]) * constants.ABILITY_COST;
	}

	/**
	 * Precondition: f should actually be directly in front of the critter.
	 * Attempts to eat as much food as possible from {@code f}.
	 * This amount is equal to size * ENERGY_PER_SIZE - energy
	 * No effect if the critter died. (No zombie critters allowed!)
	 * @param f Food object to nom on
	 */
	public void eat(Food f){
		consumeEnergy(size());
		assert(attributes[4] <= size() * constants.ENERGY_PER_SIZE);
		if(!isDead) { //can't eat if you're dead, sorry!
			int hunger = size() * constants.ENERGY_PER_SIZE - attributes[4];
			attributes[4] += f.consume(hunger);
		}
	}

	/**
	 * Absorbs light from the sun, gaining SOLAR_FLUX energy.
	 */
	public void absorb(){
		//if you get attacked and die, you're not allowed to absorb either
		if(!isDead) {
			attributes[4] += attributes[3] * constants.SOLAR_FLUX;
		}
	}

	/**
	 * Creates a food with up to {@code energy} or the critter's available remaining energy,
	 * whichever is less.
	 * @return new Food with amount {@energy}, or null if energy <= 0
	 */
	public Food serve(int energy){
		consumeEnergy(size());
		if(energy <= 0){
			return null;
		}
		int available = Math.min(attributes[4], energy);
		Food f = new Food(/*the next hex*/0, 0, available, constants);
		consumeEnergy(available);
		return f;
	}

	/**
	 * Grows the critter's size by 1 by using size * complexity * GROW_COST energy
	 */
	public void grow(){
		consumeEnergy(size() * complexity() * constants.GROW_COST);

		//no, you cannot grow, die, then dump more food on the ground
		if(!isDead){
			attributes[3]++;
		}
	}


}