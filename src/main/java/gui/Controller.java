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
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.entity.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    CloseableHttpClient httpclient = HttpClients.createDefault();
    int sessionID;
    int lastTimestep;
    

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
        /*focused.setValue(getEntityAt(c));
        setChanged();
        notifyObservers();
        System.out.println("Checkpoint1");
        System.out.println(c);
        System.out.println(focused);*/
    }

    public Entity getEntityAt(Coordinate c){
        //return sim.getEntityAt(c);
    	HttpGet get = new HttpGet("http://localhost:4567/CritterWorld/world?update_since" + lastTimestep + "&"
    	        + "from_row=" + c.getRow() + "&"
    	        + "to_row=" + c.getRow() + "&"
    	        + "from_col" + c.getCol() + "&"
    	        + "to_col" + c.getCol() + "&" 
    	        + "session_id" + sessionID);
        try{
        	CloseableHttpResponse response = httpclient.execute(get);
        	HttpEntity ent =  response.getEntity();
        	Gson gson = new GsonBuilder().create();
        	WorldState section = gson.fromJson(ent.toString(), WorldState.class);
        	if (section.getState().length > 0) {
        		return section.getState()[0];
        	}
        	else
        		return null;
        }
        catch(Exception e) {
        	System.out.println("Didn't work");
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * Adds the currently loaded Entity to the location clicked.
     * @param event The MouseEvent originating from the clicked hex
     */
    public void addEntityClick(MouseEvent event){
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
            /*sim.parseWorld(new FileReader(file));
            this.loadedWorld = sim.world;
            setChanged();
            notifyObservers();*/
        	FileReader f = new FileReader(file);
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	HttpPost post = new HttpPost("http://localhost:4567/CritterWorld/world?session_id" + sessionID);
        	FileEntity fileEnt = new FileEntity(file,ContentType.APPLICATION_JSON);
        	post.setEntity(fileEnt);
        	CloseableHttpResponse response = httpclient.execute(post);
        	
        	
        }
        catch(FileNotFoundException e){
            System.out.println("File not found"); //shouldn't happen
        }
        catch(NoSuchElementException e){
            throw new NoSuchElementException("Error while parsing file.");
        }
        catch(Exception e ) {
        	System.out.println("didn't work");
        	e.printStackTrace();
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
        if (loaded instanceof Critter){
        	loadCritter(loadedEntity);
        	//loaded.move(coordinate);
        	HttpPost post = new HttpPost("http://localhost:4567/CritterWorld/critters?session_id=" + sessionID);
            NewCritterPositions inputVar = new NewCritterPositions((Critter)loaded);
            Coordinate[] positions = new Coordinate[]{coordinate};
            inputVar.setPositions(positions);
            Gson critterGson = new GsonBuilder().registerTypeAdapter(NewCritterPositions.class, new NewCritterSerializer()).setPrettyPrinting().create();
            StringEntity myEntity = new StringEntity(critterGson.toJson(inputVar),ContentType.APPLICATION_JSON);
            post.setEntity(myEntity);
            try{
            	httpclient.execute(post);
            }
            catch(Exception e) {
            	System.out.println("didn't work");
            	e.printStackTrace();
            }
        }
        else
        	loaded.setLocation(coordinate);
        //if(sim.getEntityAt(coordinate) != null) return;
        //sim.addEntity(loaded);
        //setChanged();
        //notifyObservers();
        
       
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
            System.out.println(numTimes);
            System.out.println(getWorldCols()*getWorldRows()*0.6);
            if (numTimes > getWorldCols()*getWorldRows()*0.6){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Very Large Number");
                alert.setContentText("The number specified was very large\n World may be cluttered");
                alert.showAndWait();
            }
		    for(int i = 0; i < numTimes; i++ ) {
			    sim.addRandomEntity(loaded);
		        loadCritter(loadedEntity);
		        setChanged();
		        notifyObservers();
		    }
		    HttpPost post = new HttpPost("http://localhost:4567/CritterWorld/critters?session_id=" + sessionID);
            NewCritterPositions inputVar = new NewCritterPositions((Critter)loaded);
            inputVar.setNum(numTimes);
            Gson critterGson = new GsonBuilder().registerTypeAdapter(NewCritterPositions.class, new NewCritterSerializer()).setPrettyPrinting().create();
            StringEntity myEntity = new StringEntity(critterGson.toJson(inputVar),ContentType.APPLICATION_JSON);
            post.setEntity(myEntity);
            try{
            	httpclient.execute(post);
            }
            catch(Exception e) {
            	System.out.println("didn't work");
            	e.printStackTrace();
            }
	    }
    }


    public String getWorldName(){
        return sim.world.name;
    }

    public void advance(int n){
        /*sim.advance(n);
        setChanged();
        notifyObservers();*/
    	try{
	    	HttpPost post = new HttpPost("http://localhost:4567/CritterWorld/world?session_id=" + sessionID);
	    	StringEntity params = new StringEntity("\"count\":"+ n, ContentType.APPLICATION_JSON);
    	}
    	catch(Exception e) {
    		System.out.println("Did not work");
    		e.printStackTrace();
    	}
        
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
