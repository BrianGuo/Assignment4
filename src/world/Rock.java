package world;
//import static world.World.ROCK_


import exceptions.IllegalCoordinateException;

public class Rock extends Entity{
    public int hashCode(){
        return -1; //woohoo this actually won't conflict!
    }

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
