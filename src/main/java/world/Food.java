package world;


import exceptions.IllegalCoordinateException;

import java.util.ArrayList;

/**
 * value is always > 0
 */
public class Food extends Entity{
    private int value; //amount of food remaining

    public int hashCode(){
        return 2112 + value * 1597; //hopefully won't conflict with any critter hashcodes
    }

    private transient boolean isConsumed = false;
    @Override
    public String toString(){
        return "F";
    }

    public int getValue() {
    	return value;
    }
    public int appearance(){
        return -1* value - 1;
    }

    /**
     * Creates a new Food object on a given hex.
     * @param c the column of the Food
     * @param r the row of the Food
     * @param amt how much food to put, before multiplying by FOOD_PER_SIZE
     * @param constants constants of the world
     * @throws IllegalCoordinateException
     * @throws IllegalArgumentException
     */
    public Food(int c, int r, int amt, WorldConstants constants) throws IllegalCoordinateException, IllegalArgumentException {
        super(c,r, constants);
        if(amt <= 0){
            throw new IllegalArgumentException();
        }
        this.value = amt;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    /**
     * Precondition: amount > 0
     * Consumes up to {@code amount} food, but not more than value left.
     * If amount >= value (would consume all the food), sets isConsumed to true.
     * @param amount Amount of food to eat.
     * @return the actual amount of food consumed
     */
    public int consume(int amount){
        assert(amount > 0);
        assert(!isConsumed); //we shouldn't run into concurrency problems, so this should be false
        amount = Math.min(amount, value);
        value -= amount;
        if(value <= 0){
            isConsumed = true;
        }
        return amount;
    }

    public ArrayList<String> properties(){
        ArrayList<String> ary = new ArrayList<>();
        ary.add("Type: Food");
        ary.add("Food left: " + value);
        return ary;
    }
}
