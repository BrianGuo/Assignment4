package world;


import exceptions.IllegalCoordinateException;

/**
 * A class that represents a location on the world.
 * Class invariant: x>= 0, y >= 0
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Preserves the class invariant of x >= 0, y >= 0
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @throws IllegalCoordinateException
     */

    public void checkBounds(int x, int y) throws IllegalCoordinateException{
        if(x < 0 || y < 0){
            throw new IllegalCoordinateException();
        }
    }

    /**
     * Changes the coordinate of an Entity from one location to another.
     * @param destX The new X coordinate to move to
     * @param destY The new Y coordinate to move to
     * @throws IllegalCoordinateException if (destX, destY) is an illegal point
     */
    public void move(int destX, int destY) throws IllegalCoordinateException {
        checkBounds(destX, destY);
        x = destX;
        y = destY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
