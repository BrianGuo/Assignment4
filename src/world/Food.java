package world;


import exceptions.IllegalCoordinateException;

/**
 * amt is always > 0
 */
public class Food extends Entity{
    private int amt; //amount of food remaining

    public int hashCode(){
        return 2112 + amt * 1597; //hopefully won't conflict with any critter hashcodes
    }

    private boolean isConsumed = false;
    @Override
    public String toString(){
        return "F";
    }

    public int getAmt() {
    	return amt;
    }
    public int appearance(){
        return -1* amt - 1;
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
        this.amt = amt;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    /**
     * Precondition: amount > 0
     * Consumes up to {@code amount} food, but not more than amt left.
     * If amount >= amt (would consume all the food), sets isConsumed to true.
     * @param amount Amount of food to eat.
     * @return the actual amount of food consumed
     */
    public int consume(int amount){
        assert(amount > 0);
        assert(!isConsumed); //we shouldn't run into concurrency problems, so this should be false
        amount = Math.min(amount, amt);
        amt -= amount;
        if(amt <= 0){
            isConsumed = true;
        }
        return amount;
    }
}
