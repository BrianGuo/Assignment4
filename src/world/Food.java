package world;


import exceptions.IllegalCoordinateException;

/**
 * amt is always > 0
 */
public class Food extends Entity{
    private int amt; //amount of food remaining
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
    public Food(int c, int r, int amt) throws IllegalCoordinateException, IllegalArgumentException {
        super(c,r);
        if(amt <= 0){
            throw new IllegalArgumentException();
        }
        this.amt = amt;
    }
}
