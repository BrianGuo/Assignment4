package world;


import exceptions.IllegalCoordinateException;

/**
 * A class that represents a location on the world.
 * Class invariant: col>= 0, y >= 0
 */
public class Coordinate {
    private int col;
    private int row;

    public Coordinate(int col, int row) throws IllegalCoordinateException{
        checkBounds(col,row);
        this.col = col;
        this.row = row;
    }

    /**
     * Preserves the class invariant of col >= 0, row >= 0
     * @param col X coordinate of the point
     * @param row Y coordinate of the point
     * @throws IllegalCoordinateException
     */

    public void checkBounds(int col, int row) throws IllegalCoordinateException{
        if(col < 0 || row < 0){
            throw new IllegalCoordinateException("Negative value for either column or row");
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
        col = destX;
        row = destY;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    
    public String toString() {
    	return "[" + col +","+ row + "]";
    }

}
