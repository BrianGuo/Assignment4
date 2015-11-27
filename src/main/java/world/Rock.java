package world;
//import static world.World.ROCK_


import exceptions.IllegalCoordinateException;

import java.util.ArrayList;

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

    @Override
    public ArrayList<String> properties() {
        ArrayList<String> ary = new ArrayList<>();
        ary.add("Type: Rock");
        return ary;
    }


}
