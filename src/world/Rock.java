package world;
//import static world.World.ROCK_


import exceptions.IllegalCoordinateException;

public class Rock extends Entity{
    @Override
    public String toString() {
        return "#";
    }

    @Override
    public int appearance() {
        return constants.ROCK_VALUE;
    }

    public Rock(int c, int r, WorldConstants constants) throws IllegalCoordinateException{
        super(c,r, constants);
    }
}
