package gui;

import exceptions.IllegalOperationException;
import exceptions.SyntaxError;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import simulator.Simulator;
import world.Coordinate;
import world.Critter;
import world.Entity;
import world.Factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Max on 11/8/2015.
 */
public class Controller {
    Simulator sim;
    Entity focused;
    Entity loaded;

    public Controller(){
        sim = new Simulator();
        //sim.parseWorld("world.txt");
    }


    /**
     * Gets the coordinates of the hex clicked on the map.
     * @param event Mouseevent originating from the clicked hex
     * @return The Coordinate of the clicked hex
     */
    public Coordinate handleHexClick(MouseEvent event){
        WorldHex clicked = (WorldHex) event.getSource();
        return clicked.getCoordinate();
    }

    /**
     * Adds the currently loaded Entity to the location clicked.
     * @param event The MouseEvent originating from the clicked hex
     */
    public void addEntityClick(MouseEvent event){
        Coordinate c = handleHexClick(event);
        addEntity(c);
    }

    /**
     * Loads a world into the underlying simulator.
     * @param file File to load the world from.
     */
    public void loadWorld(File file){
        try {
            sim.parseWorld(new FileReader(file));
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    /**
     * Loads a critter into the app to be added later.
     * @param filename Filename of the critter
     */
    public void loadCritter(String filename){
        System.out.println(filename);
        try{
            Critter c = Factory.getCritter(filename, sim.world.constants);
            this.loaded = c;
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(SyntaxError e){
            //...
        }
    }

    /**
     * Adds the currently loaded entity into the world at the desired coordinate.
     * @param coordinate Coordinate to be added
     */
    public void addEntity(Coordinate coordinate){
        loaded.move(coordinate);
        if(sim.hexAt(coordinate) == null) return;
        sim.addEntity(loaded);
    }

    /**
     * Adds the currently loaded entity into the world at a random location.
     */
    public void addRandomEntity(ActionEvent event){
        if(loaded == null) throw new IllegalOperationException("No entity loaded!");
        sim.addRandomEntity(loaded);

    }


}
