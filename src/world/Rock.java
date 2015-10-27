package world;


import exceptions.IllegalCoordinateException;

public class Rock extends Entity{
    @Override
    public String toString() {
        return "#";
    }

    @Override
    public int appearance() {
        return -1;
    }

    public Rock(int c, int r) throws IllegalCoordinateException{
        super(c,r);
    }
}
