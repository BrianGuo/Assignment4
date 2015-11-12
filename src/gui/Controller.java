package gui;


import exceptions.IllegalOperationException;
import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import simulator.Simulator;
import world.Coordinate;
import world.Critter;
import world.Entity;
import world.Factory;
import world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Max on 11/8/2015.
 */
public class Controller extends java.util.Observable {
	Random r = new Random();
	Image[] critterImages;
	App App;
    Simulator sim;
    //Entity focused;
    Entity loaded;
    ObjectProperty<Entity> focused; //gdi this doesn't work
    String loadedEntity = "";

    public Controller(){
        sim = new Simulator(new CritterInterpreter());
        focused = new SimpleObjectProperty<>();
        try{
    		critterImages = new Image[]{new Image(new FileInputStream(new File("BirdCritter.png"))),new Image(new FileInputStream(new File("CatCritter.png"))),new Image(new FileInputStream(new File("PenguinCritter.png"))),new Image(new FileInputStream(new File("MooseCritter.png")))};
    	}
    	catch(FileNotFoundException e) {
    		System.out.println("File Not Found");
    	}
        Timeline simTimeline = new Timeline();
       /* simTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300)),
        e -> {

        })*/
    }


    /**
     * Gets the coordinates of the hex clicked on the map.
     * @param event Mouseevent originating from the clicked hex
     * @return The Coordinate of the clicked hex
     */
    public Coordinate handleHexClick(MouseEvent event){
        WorldHex clicked = (WorldHex) event.getSource();
        //focused.set(sim.getEntityAt(clicked.getCoordinate()));
        return clicked.getCoordinate();
    }

    public void handleFocusClick(MouseEvent event){
        Coordinate c = handleHexClick(event);
        focused.setValue(getEntityAt(c));
        setChanged();
        notifyObservers();
        System.out.println("Checkpoint1");
        System.out.println(c);
    }

    public Entity getEntityAt(Coordinate c){
        return sim.getEntityAt(c);
    }

    /**
     * Adds the currently loaded Entity to the location clicked.
     * @param event The MouseEvent originating from the clicked hex
     */
    public void addEntityClick(MouseEvent event){
        if(!sim.hasWorld()){
            throw new IllegalOperationException("You must load a world first.");
        }
        if(loaded == null){
            throw new IllegalOperationException("You must load a critter first.");
        }
        
        Coordinate c = handleHexClick(event);
        System.out.println(c);
        try {
            addEntity(c, (WorldHex) event.getSource());
        }
        catch(ArrayIndexOutOfBoundsException e){
            throw new IllegalOperationException("Clicked outside of the world.");
        }
        setChanged();
        notifyObservers();
    }
    
    public void setWorld(World w) {
    	sim.setWorld(w);
    }

    /**
     * Loads a world into the underlying simulator.
     * @param file File to load the world from.
     */
    public void loadWorld(File file){
        try {
            sim.parseWorld(new FileReader(file));
            setChanged();
            notifyObservers();
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
        loadedEntity = filename;
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
    public void addEntity(Coordinate coordinate, WorldHex w){
        loaded.move(coordinate);
        if(sim.getEntityAt(coordinate) != null) return;
        Image img = critterImages[r.nextInt(4)];
        w.setFill(new ImagePattern(img, 0, 0, 1, 1, true));
        sim.addEntity(loaded);
        loadCritter(loadedEntity);
        setChanged();
        notifyObservers();
    }

    /**
     * Adds the currently loaded entity into the world at a random location.
     */
    public void addRandomEntity(ActionEvent event){
        if(loaded == null) throw new IllegalOperationException("No entity loaded!");
        sim.addRandomEntity(loaded);
        loadCritter(loadedEntity);
        setChanged();
        notifyObservers();
    }


    public String getWorldName(){
        return sim.world.name;
    }

    public void advance(int n){
        sim.advance(n);
        setChanged();
        notifyObservers();
    }

    /**
     * Called continuously when advanced continuously
     * Does NOT notify observers; that is called manually
     */
    public void advanceContinuously(){
        sim.advance(1);
    }

    public ArrayList<Coordinate> diffWorld(){
        return sim.diffWorld();
    }
    
    public int getWorldCols() {
    	return sim.getWorldColumns();
    }
    public int getWorldRows() {
    	return sim.getWorldRows();
    }
}
