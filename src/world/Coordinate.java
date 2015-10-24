package world;


import exceptions.IllegalCoordinateException;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void move(int destX, int destY) throws IllegalCoordinateException {
        if(destX < 0 || destY < 0){
            throw new IllegalCoordinateException();
        }
        else{
            x = destX;
            y = destY;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
