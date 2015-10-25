package world;


import exceptions.IllegalCoordinateException;

public class Rock extends Entity{
    @Override
    public String toString() {
        //TODO
        return null;
    }

    @Override
    public int appearance() {
        //TODO
        return 0;
    }

    public Rock(int c, int r) throws IllegalCoordinateException{
        super(c,r);
    }
}
