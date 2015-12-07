package gui;

import java.lang.reflect.Type;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import world.Coordinate;
import world.Critter;

public class NewCritterSerializer implements JsonSerializer<NewCritterPositions> {

	@Override
	public JsonElement serialize(NewCritterPositions critterObject, Type arg1, JsonSerializationContext arg2) {
		JsonObject object = new JsonObject();
		Critter critter = critterObject.getCritter();
		if (critter.getSpecies() != null){
			String species_id = critter.getSpecies();
			object.addProperty("species_id", species_id);
		}
        object.addProperty("program", critter.getProgram().toString());
        object.addProperty("mem", Arrays.toString(critter.getMemory()));
        if (critterObject.getPositions() != null) {
        	Coordinate[] positions = critterObject.getPositions();
        	JsonArray arr = new JsonArray();
        	for (Coordinate coordinate : positions) {
        		JsonObject object2 = new JsonObject();
        		object2.add("row", new JsonPrimitive(coordinate.getRow()));
        		object2.add("col", new JsonPrimitive(coordinate.getCol()));
        		arr.add(object2);
        	}
        	object.add("positions", arr);
        }
        else if (critterObject.getNum() > 0){
        	object.addProperty("num", critterObject.getNum());
        }
        return object;
	}
	
	
}