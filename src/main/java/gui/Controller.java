package gui;


import exceptions.IllegalOperationException;
import interpret.CritterInterpreter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import parse.Parser;
import parse.ParserFactory;
import simulator.Simulator;
import world.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ast.Program;

public class Controller extends java.util.Observable {
	Random r = new Random();
	Image[] critterImages;
	App App;
    Simulator sim;
    //Entity focused;
    Entity loaded;
    World loadedWorld;
    ObjectProperty<WorldState> focused; //gdi this doesn't work
    String loadedEntity = "";
    final Timeline simTimeline;
    final Timeline UITimeline;
    CloseableHttpClient httpclient = HttpClients.createDefault();
    int sessionID;
    //int lastVersion;
    String serverURL;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    WorldConstants constants;
    
    

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
        try{
        	constants = new WorldConstants(new FileReader("constants.txt"));
        }
        catch(Exception e){
        	System.out.print("Where's your constants file?");
        }
       /* simTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300)),
        e -> {

        })*/
    }
    
    public void setServerURL(String s) {
    	serverURL = s;
    }
    
    public void validate(Optional<ArrayList<String>> session) {
    	try{
    		serverURL = session.get().get(0);
	    	HttpPost post = new HttpPost(serverURL + "/CritterWorld/login");
	    	StringEntity ent = new StringEntity("{\"level\":" + session.get().get(1) + "," + "\"password\":" + session.get().get(2) + "}", ContentType.APPLICATION_JSON);
	    	System.out.println(ent.getContent().read());
	    	post.setEntity(ent);
	    	CloseableHttpResponse response = httpclient.execute(post);
	    	System.out.println(response.toString());
	    	HttpEntity e = response.getEntity();
	    	System.out.println("here");
	    	String est = EntityUtils.toString(e);
	    	System.out.println(e);
	    	Gson gson = new GsonBuilder().create();
	    	Map map = gson.fromJson(est, Map.class);
	    	System.out.println(map.get("session_id"));
	    	sessionID =  ((Double)map.get("session_id")).intValue();
	    	System.out.println(sessionID);
    	}
    	catch(Exception e) {
    		System.out.println("didn't work");
    		e.printStackTrace();
    	}
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

    public void handleFocusClick(MouseEvent event, int lastVersion){
        Coordinate c = handleHexClick(event);
        focused.setValue(getEntityAt(c, lastVersion));
        setChanged();
        notifyObservers();
        /*
        System.out.println("Checkpoint1");
        System.out.println(c);
        System.out.println(focused);*/
    }

    public WorldState getEntityAt(Coordinate c, int lastVersion){
        //return sim.getEntityAt(c);
    	HttpGet get = new HttpGet(serverURL + "/CritterWorld/world?" 
    	        + "from_row=" + c.getRow() + "&"
    	        + "to_row=" + c.getRow() + "&"
    	        + "from_col=" + c.getCol() + "&"
    	        + "to_col=" + c.getCol() + "&" 
    	        + "session_id=" + sessionID);
        try{
        	CloseableHttpResponse response = httpclient.execute(get);
        	HttpEntity ent =  response.getEntity();
        	Gson gson = new GsonBuilder().create();
        	System.out.println("^^");
        	String e = EntityUtils.toString(ent);
        	System.out.println(e);
        	System.out.println("Right here^^");
        	WorldState section = gson.fromJson(e , WorldState.class);
        	response.close();
        	ArrayList<HexEntity> entities = section.getState();
        	ArrayList<Entity> state2 = new ArrayList<Entity>();
	    	for (HexEntity m : entities) {
	    		int row = m.getRow();
	    		int col = m.getCol();
	    		switch(m.getType()){
	    		case "rock":
	    			Rock r = new Rock(col, row, null);
	    			state2.add(r);
	    			break;
	    		case "food":
	    			Food f = new Food(col, row, m.getValue(), null);
	    			state2.add(f);
	    			break;
	    		case "critter":
	    			System.out.println(m.getMem());
	    			Parser parser = ParserFactory.getParser();
	    			Program p = parser.parse(new StringReader(m.getProgram()));
	    			Critter cr = new Critter(m.getMem(), m.getDirection(), m.getSpecID(), new Coordinate(col, row),null, p);
	    			state2.add(cr);
	    			break;
	    		case "nothing":
	    			state2.add(new Nothing(m.getCol(), m.getRow()));
	    			break;
	    		default:
	    			break;
	    		}
	    	}
	    	section.setRefactored(state2);
	    	return section;
        	
        	/*if (section.getState().size() > 0) {
        		HexEntity h = section.getState().get(0);
        		int row = h.getRow();
        		int col = h.getCol();
        		switch(section.getState().get(0).getType()){
	    		case "rock":
	    			Rock r = new Rock(col, row, null);
	    			return r;
	    		case "food":
	    			Food f = new Food(col, row, h.getValue(), null);
	    			return f;
	    		case "critter":
	    			Critter cr = new Critter(h.getMem(), h.getDirection(), h.getSpecID(), new Coordinate(col, row), null, null);
	    			return cr;
	    		case "nothing":
	    			return new Nothing(col,row);    			
	    		default:
	    			return null;
	    		}
        	}
        	else
        		return null;*/
        	
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
        	HttpPost post = new HttpPost(serverURL + "/CritterWorld/world?session_id=" + sessionID);
        	BufferedReader reader = new BufferedReader(new FileReader(file));
        	String description = "";
        	String next = reader.readLine();
        	while(next != null) {
        		description += next + "\n";
        		next = reader.readLine(); 
        	}
        	//StringEntity entity = new StringEntity("{\"description\":" +" \"" + description + "}");
        	JsonObject je = new JsonObject();
        	je.addProperty("description", description);
        	StringEntity entity = new StringEntity(gson.toJson(je));
        	
        	System.out.println(EntityUtils.toString(entity));
        	post.setEntity(entity);
        	CloseableHttpResponse response = httpclient.execute(post);
        	System.out.println("loadWorldResponse:" + response);
        	setChanged();
        	notifyObservers();
        	reader.close();
        	response.close();
        	
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
            Critter c = Factory.getCritter(filename, constants);
            this.loaded = c;

        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(Exception e){
        	e.printStackTrace();
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
        	HttpPost post = new HttpPost(serverURL + "/CritterWorld/critters?session_id=" + sessionID);
            NewCritterPositions inputVar = new NewCritterPositions((Critter)loaded);
            Coordinate[] positions = new Coordinate[]{coordinate};
            inputVar.setPositions(positions);
            Gson critterGson = new GsonBuilder().registerTypeAdapter(NewCritterPositions.class, new NewCritterSerializer()).setPrettyPrinting().create();
            StringEntity myEntity = new StringEntity(critterGson.toJson(inputVar),ContentType.APPLICATION_JSON);
            post.setEntity(myEntity);
            try{
            	CloseableHttpResponse response = httpclient.execute(post);
            	System.out.println("Entity Click Response:" + response);
            	response.close();
            }
            catch(Exception e) {
            	System.out.println("didn't work");
            	e.printStackTrace();
            }
        }
        else if (loaded instanceof Food || loaded instanceof Rock){
        	loaded.setLocation(coordinate);
        	HttpPost post = new HttpPost(serverURL + "/CritterWorld/world/create_entity?session_id=" + sessionID);
        	JsonObject jo = new JsonObject();
        	jo.addProperty("row", coordinate.getRow());
        	jo.addProperty("col", coordinate.getCol());
        	if (loaded instanceof Food){
        		jo.addProperty("type", "food");
        		jo.addProperty("amount", ((Food) loaded).getValue());
        	}
        	else
        		jo.addProperty("type", "rock");
        	try {
				StringEntity myEntity = new StringEntity (gson.toJson(jo));
				post.setEntity(myEntity);
				CloseableHttpResponse response = httpclient.execute(post);
				response.close();
				setChanged();
				notifyObservers();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        //if(sim.getEntityAt(coordinate) != null) return;
        //sim.addEntity(loaded);
        setChanged();
        notifyObservers();
        
       
    }

    /**
     * Adds the currently loaded entity into the world at a random location.
     * @throws IOException 
     * @throws ParseException 
     */
    public void addRandomEntity(ActionEvent event, String num) throws ParseException{
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
            System.out.println("?");
		    /*for(int i = 0; i < numTimes; i++ ) {
			    sim.addRandomEntity(loaded);
		        loadCritter(loadedEntity);
		        setChanged();
		        notifyObservers();
		    }*/
		    HttpPost post = new HttpPost(serverURL + "/CritterWorld/critters?session_id=" + sessionID);
            NewCritterPositions inputVar = new NewCritterPositions((Critter)loaded);
            inputVar.setNum(Integer.parseInt(num));
            Gson critterGson = new GsonBuilder().registerTypeAdapter(NewCritterPositions.class, new NewCritterSerializer()).setPrettyPrinting().create();
            StringEntity myEntity = new StringEntity(critterGson.toJson(inputVar),ContentType.APPLICATION_JSON);
            System.out.println("??");
            post.setEntity(myEntity);
            try{
            	System.out.println("___");
            	System.out.println(critterGson.toJson(inputVar));
            	System.out.println("^^");
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
            
            try{
            	CloseableHttpResponse response = httpclient.execute(post);
            	response.close();
            	setChanged();
            	notifyObservers();
            	System.out.println("completed");
            	
            	
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

    public WorldState updateWorld(int lastVersion) {
    	try{
	    	HttpGet get = new HttpGet(serverURL + "/CritterWorld/world?session_id=" + sessionID + "&update_since=" + lastVersion);
	    	CloseableHttpResponse response = httpclient.execute(get);
	    	HttpEntity e = response.getEntity();
	    	System.out.println(response);
	    	String responsestring = EntityUtils.toString(e);
	    	System.out.println(responsestring);
	    	WorldState state = gson.fromJson(responsestring, WorldState.class);
	    	System.out.println(state.getCols() + "this is the number of cols");
	    	System.out.println(state.getRows() + "this is the number of rows");
	    	System.out.println(state.getTimestep() + "this is the number of timeSteps");
	    	ArrayList<HexEntity> entities =  state.getState();
	    	ArrayList<Entity> state2 = new ArrayList<Entity>();
	    	for (HexEntity m : entities) {
	    		int row = m.getRow();
	    		int col = m.getCol();
	    		switch(m.getType()){
	    		case "rock":
	    			Rock r = new Rock(col, row, null);
	    			state2.add(r);
	    			break;
	    		case "food":
	    			Food f = new Food(col, row, m.getValue(), null);
	    			state2.add(f);
	    			break;
	    		case "critter":
	    			System.out.println(m.getMem());
	    			Critter cr = new Critter(m.getMem(), m.getDirection(), m.getSpecID(), new Coordinate(col, row), null, null);
	    			state2.add(cr);
	    			break;
	    		case "nothing":
	    			state2.add(new Nothing(m.getCol(), m.getRow()));
	    			break;
	    		default:
	    			break;
	    		}
	    	}
	    	state.setRefactored(state2);
	    	response.close();
	    	return state;
    	}
    	catch(Exception e) {
    		System.out.println("didn't work");
    		e.printStackTrace();
    		return null;
    	}
    }
    public void advance(int n){
        /*sim.advance(n);
        setChanged();
        notifyObservers();*/
    	try{
	    	HttpPost post = new HttpPost(serverURL + "/CritterWorld/step?session_id=" + sessionID);
	    	JsonObject jo = new JsonObject();
	    	jo.addProperty("count", n);
	    	StringEntity params = new StringEntity(gson.toJson(jo), ContentType.APPLICATION_JSON);
	    	System.out.println(1);
	    	post.setEntity(params);
			System.out.println(2);
	    	CloseableHttpResponse response = httpclient.execute(post);
	    	response.close();
			System.out.println(3);
	    	setChanged();
	    	notifyObservers();
	    	
	    	System.out.println("got here 2");
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
    synchronized public void advanceContinuously(double speed){
        try{
        	HttpPost post = new HttpPost(serverURL + "/CritterWorld/run?session_id=" + sessionID);
        	StringEntity entity = new StringEntity("{\"rate\":" + speed + "}");
            post.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(post);
            response.close();
        }
        catch(Exception e) {
        	System.out.println("Didn't work");
        	e.printStackTrace();
        }
        
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
                advanceContinuously(speed);
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
