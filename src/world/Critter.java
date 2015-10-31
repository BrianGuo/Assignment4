package world;

import java.util.ArrayList;
import java.util.Random;

import ast.Program;
import ast.ProgramImpl;
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
	Critter lover;
	private Critter lovedBy;

	//keeps track of whether the critter is dead

	boolean isDead = false;

	//keeps track of whether the critter is mating
	//boolean isMating = false;


	public Critter(int[] attributes, int direction, String species, Coordinate coordinates, WorldConstants constants,
				   Program program) {
		super(constants);
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
	public void consumeEnergy(int amt){
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
		ArrayList<Rule> n = p.getRules();
		int a = 0;
		if (n != null)
			a = n.size();
		if (p == null)
			System.out.println("p is null");
		if (constants == null)
			System.out.println("constants is null");
		if (attributes == null) {
			System.out.println("Attributes is null");
		}
		return p.getRules().size() * constants.RULE_COST + (attributes[1] + attributes[2]) * constants.ABILITY_COST;
	}

	/**
	 * Precondition: f should actually be directly in front of the critter.
	 * Attempts to eat as much food as possible from {@code f}.
	 * This amount is equal to size * ENERGY_PER_SIZE - energy
	 * No effect if the critter died. (No zombie critters allowed!)
	 * @param f Food object to nom on
	 * @return the food eaten, or null if it's not food
	 */
	public Food eat(Entity f){
		consumeEnergy(size());
		Food food;
		if (f instanceof Food) { // :(
			food = (Food) f;
			assert (attributes[4] <= size() * constants.ENERGY_PER_SIZE);
			if (!isDead) { //can't eat if you're dead, sorry!
				int hunger = size() * constants.ENERGY_PER_SIZE - attributes[4];
				attributes[4] += food.consume(hunger);
				return food;
			}

		}
		return null;
	}

	/**
	 * Absorbs light from the sun, gaining SOLAR_FLUX energy.
	 */
	public void absorb(){
		//if you get attacked and die, you're not allowed to absorb either
		if(!isDead) {
			attributes[4] += attributes[3] * constants.SOLAR_FLUX;
			//can't go above maximum energy
			attributes[4] = Math.min(attributes[4], size() * constants.ENERGY_PER_SIZE);
		}
	}

	/**
	 * Creates a food with up to {@code energy} or the critter's available remaining energy,
	 * whichever is less.
	 * @return new Food with amount {@energy}, or null if energy <= 0
	 */
	public Food serve(int energy, Coordinate newLocation){
		consumeEnergy(size());
		if(energy <= 0){
			return null;
		}
		int available = Math.min(attributes[4], energy);
		Food f = new Food(newLocation.getCol(), newLocation.getRow(), available, constants);
		consumeEnergy(available);
		return f;
	}

	/**
	 * Grows the critter's size by 1 by using size * complexity * GROW_COST energy
	 * If the critter dies trying to grow, it dumps food using the previous size, even if it was successful.
	 */
	public void grow(){
		consumeEnergy(size() * complexity() * constants.GROW_COST);

		//no, you cannot grow, die, then dump more food on the ground
		if(!isDead){
			attributes[3]++;
		}
	}

	public void tag(Entity other, int tag) {
		consumeEnergy(size());
		Critter otherC;
		if (!isDead) {
			if (other instanceof Critter) {
				otherC = (Critter) other;
				if (tag >= 0 && tag <= 99) {
					otherC.attributes[6] = tag;
				}
			}
		}

	}

	/**
	 * Attempts to attack the enemy
	 * @param enemy the enemy critter to attack
	 * @return the enemy critter attacked, or null if it wasn't a critter
	 */
	public Critter attack(Entity enemy){
		Critter enemyC;
		consumeEnergy(size() * constants.ATTACK_COST);
		if(enemy instanceof Critter) {
			enemyC = (Critter) enemy;
			//if you're too hungry to successfully attack you can't hit them while dying
			if (!isDead) {
				int damageDealt = calculateDamage(size(), enemyC.size(), attributes[2], enemyC.attributes[1]);
				enemyC.consumeEnergy(damageDealt);
				return enemyC;
			}

		}
		return null;
	}

	/**
	 * Calculates damage done given some parameters.
	 * @param s1 size of attacking critter
	 * @param s2 size of defending critter
	 * @param o1 offense stat of attacking critter
	 * @param d2 defense stat of defending critter
	 * @return damage done, rounded half up.
	 */
	private int calculateDamage(int s1, int s2, int o1, int d2){
		return (int) Math.round(constants.BASE_DAMAGE * s1 * logistic(constants.DAMAGE_INC * (s1*o1 - s2*d2)));
	}

	/**
	 * Moves the critter to a new location.
	 * @param newLocation The new location to move to.
	 */
	public void move(Coordinate newLocation){
		consumeEnergy(size() * constants.MOVE_COST);
		//dead critters can't move either
		if(!isDead){
			this.location = newLocation;
		}
	}

	public Critter bud(Coordinate babyLocation) {
		int[] newAttributes = new int[attributes[0]];
		//copy over memsize, zero out general purpose fields
		newAttributes[0] = attributes[0];
		newAttributes[1] = 1;
		newAttributes[2] = 1;
		newAttributes[3] = 1;
		newAttributes[4] = constants.INITIAL_ENERGY;
		newAttributes[5] = 0;
		newAttributes[6] = 0;
		newAttributes[7] = 0; //just to be clear that we're resetting this

		consumeEnergy(complexity() * constants.BUD_COST);
		if (!isDead && babyLocation != null){
			Critter baby = new Critter(newAttributes, (int)(Math.random() * 6), this.species, babyLocation,
					this.constants, this.p);
			baby.mutate();
			return baby;
		}
		return null;

	}

	/**
	 * Creates a new critter that's the child of this critter and other
	 * @param babyLocation location of the new baby
	 * @param other other parent
	 * @return the created offspring
	 */
	public Critter mate(Coordinate babyLocation, Critter other){
		if(isDead){
			other.consumeEnergy(size());
			return null;
		} //even love can't help you if someone kills you :(
		if(babyLocation == null) {
			consumeEnergy(size());
			other.consumeEnergy(size());
			return null;
		}
		int[] newAttributes = new int[attributes[0]];
		//copy over memsize, zero out general purpose fields
		newAttributes[0] = attributes[0];
		newAttributes[1] = 1;
		newAttributes[2] = 1;
		newAttributes[3] = 1;
		newAttributes[4] = constants.INITIAL_ENERGY;
		newAttributes[5] = 0;
		newAttributes[6] = 0;
		newAttributes[7] = 0; //just to be clear that we're resetting this

		//merge the programs
		Program newProgram = new ProgramImpl();
		for(Rule r: other.getProgram().getRules()){
			newProgram.getRules().add(r);
		}
		for(Rule r: p.getRules()){
			newProgram.getRules().add(r);
		}

		String thisSpecies="";
		for(int i = 0; i < species.length(); i++){
			if(Math.random() < 0.5)
				thisSpecies += species.charAt(i);
		}

		String otherSpecies="";
		for(int i = 0; i < other.species.length(); i++){
			if(Math.random() < 0.5)
				otherSpecies += other.species.charAt(i);
		}
		String newSpecies = thisSpecies + otherSpecies;
		Critter baby = new Critter(this.attributes, (int) (Math.random() * 6), newSpecies,
				babyLocation, constants, newProgram);

		//in an act of pure love, the parents can spend the last of their energy to ensure their child
		//has a chance at experiencing the world
		consumeEnergy(complexity() * constants.MATE_COST);
		other.consumeEnergy(complexity() * constants.MATE_COST);
		//of course they can't stand each other after the baby's born
		fallOutofLove();
		other.fallOutofLove();
		return baby;
	}

	/**
	 * Makes the other critter fall in loveeeeeeeee
	 * @param other other critter
	 * @param babyLocation where the baby would be born
	 */
	public Critter woo(Entity other, Coordinate babyLocation){
		Critter otherC;
		if(other instanceof Critter) {
			otherC = (Critter) other;
			if (!isDead) {
				if(lover().equals(otherC) && otherC.lover().equals(this)){ //they love me and i love them
					return mate(babyLocation, otherC);
				}
				else {
					lover = otherC;
				}
			}

		}
		return null;
	}

	public void fallOutofLove(){
		lover = null;

	}

	public Critter lover(){
		return lover;
	}

	public Critter lovedBy(){
		return lovedBy;
	}

}