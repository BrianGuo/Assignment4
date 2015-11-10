package gui;

import javafx.scene.shape.Polygon;
import world.Coordinate;

/**
 * A polygon object that contains a coordinate location
 */
public class WorldHex extends Polygon {
    Coordinate coordinate;

    public WorldHex(double... points){
        super(points);
    }
    public void setCoordinate(int col, int row){
        coordinate = new Coordinate(col, row);
    }
    public Coordinate getCoordinate(){
        return coordinate;
    }

}
