package world;

import exceptions.IllegalCoordinateException;

public class Factory {
    public static World getWorld( String cols, String rows, String name){
        return new World(Integer.parseInt(cols), Integer.parseInt(rows), name);
    }

    public static Critter getCritter(String file, String col, String row, String direction) throws IllegalCoordinateException{
        //TODO
        return null;
    }

    public static Rock getRock(String col, String row) throws IllegalCoordinateException{
        return new Rock(Integer.parseInt(col), Integer.parseInt(row));
    }

    public static Food getFood(String col, String row, String amt) throws IllegalCoordinateException{
        //TODO: Implement FOOD_PER_SIZE
        return new Food(Integer.parseInt(col), Integer.parseInt(row), Integer.parseInt(amt));
    }

}
