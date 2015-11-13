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
import javafx.scene.control.Alert;
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
import java.util.NoSuchElementException;
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
    World loadedWorld;
    ObjectProperty<Entity> focused; //gdi this doesn't work
    String loadedEntity = "";
    final Timeline simTimeline;
    final Timeline UITimeline;

    public Controller(){
        sim = new Simulator(new CritterInterpreter());
        focused = new SimpleObjectProperty<>();
        try{
    		critterImages = new Image[]{new Image(new FileInputStream(new File("BirdCritter.png"))),new Image(new FileInputStream(new File("CatCritter.png"))),new Image(new FileInputStream(new File("PenguinCritter.png"))),new Image(new FileInputStream(new File("MooseCritter.png")))};
    	}
    	catch(FileNotFoundException e) {
    		System.out.println("File Not Found");
    	}
        simTimeline = new Timeline();
        UITimeline = new Timeline();
        simTimeline.setCycleCount(Timeline.INDEFINITE);
        UITimeline.setCycleCount(Timeline.INDEFINITE);
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
        System.out.println(focused);
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
        if(loaded != null){
        	Coordinate c = handleHexClick(event);
        	try {
                addEntity(c, (WorldHex) event.getSource());
            }
            catch(ArrayIndexOutOfBoundsException e){
                throw new IllegalOperationException("Clicked outside of the world.");
            }
        	notifyObservers();
            setChanged();
        }
        else
        	throw new IllegalOperationException("You must select something to add");
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
            this.loadedWorld = sim.world;
            setChanged();
            notifyObservers();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found"); //shouldn't happen
        }
        catch(NoSuchElementException e){
            throw new NoSuchElementException("Error while parsing file.");
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
        catch(Exception e){
            throw new RuntimeException("Syntax error in chosen file");
        }
    }

    /**
     * Adds the currently loaded entity into the world at the desired coordinate.
     * @param coordinate Coordinate to be added
     */
    public void addEntity(Coordinate coordinate, WorldHex w){
        if (loaded instanceof Critter)
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
    public void addRandomEntity(ActionEvent event, String num){
    	int numTimes = 0;
    	if (num == null){
    		Alert alert = new Alert(Alert.AlertType.ERROR);
        	alert.setTitle("Invalid Field");
        	alert.setContentText("You must specify a number of Critters to put");
        	alert.showAndWait();
    	}
    	else {
    		try{
    			numTimes = Integer.parseInt(num);
    		}
    		catch(NumberFormatException e) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
            	alert.setTitle("Invalid Field");
            	alert.setContentText("You must input a number");
            	alert.showAndWait();
    		}
    	}
    	if(loaded == null) throw new IllegalOperationException("No entity loaded!");
    	else {
		    for(int i = 0; i < numTimes; i++ ) {
			    sim.addRandomEntity(loaded);
			        loadCritter(loadedEntity);
			        setChanged();
			        notifyObservers();
		    }
		    if (numTimes > getWorldCols()*getWorldRows()*0.8){
    			Alert alert = new Alert(Alert.AlertType.WARNING);
            	alert.setTitle("Very Large Number");
            	alert.setContentText("The number specified was very large\n World may be cluttered");
            	alert.showAndWait();
    		}
	    }
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
    synchronized public void advanceContinuously(){
        sim.advance(1);
        //System.out.println("?");
    }

    synchronized public ArrayList<Coordinate> diffWorld(){
        return sim.diffWorld();
    }
    
    public int getWorldCols() {
    	return sim.getWorldColumns();
    }
    public int getWorldRows() {
    	return sim.getWorldRows();
    }

    /**
     * Starts stepping the world continuously, at speed steps per second.
     * Alternatively, changes the speed of the world to speed steps per second if already running.
     * @param speed Speed to step at.
     */
    public void play(double speed){
        System.out.println(speed);
        //the world update speed
        KeyFrame k1;
        KeyFrame k2;
        if(speed != 0.0) {
            k1 = new KeyFrame(Duration.millis(1000 / speed), event -> {
                advanceContinuously();
            });
            k2 = new KeyFrame(Duration.millis(1000 / 30.0), event -> {
                setChanged();
                notifyObservers();
            });
        }
        else{
            k1 = new KeyFrame(Duration.millis(1000), event -> {
                //dummy KeyFrame that doesn't advance anything
            });
            k2 = k1;
        }
        //to update the UI

        simTimeline.stop();
        UITimeline.stop();
        simTimeline.getKeyFrames().clear();
        UITimeline.getKeyFrames().clear();
        simTimeline.getKeyFrames().add(k1);
        UITimeline.getKeyFrames().add(k2);
        simTimeline.play();
        UITimeline.play();
    }
    public void stop(){
        simTimeline.stop();
        UITimeline.stop();
    }

}
