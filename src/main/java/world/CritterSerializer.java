package world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by ball9 on 12/3/2015.
 */
public class CritterSerializer implements JsonSerializer<Critter> {
    @Override
    public JsonElement serialize(Critter critter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        String id = String.valueOf(critter.getId());
        String species_id = critter.getSpecies().replace(' ', '_'); //sanitize spaces
        String program = critter.getProgram().toString();
        String row = String.valueOf(critter.getLocation().getRow());
        String col = String.valueOf(critter.getLocation().getCol());
        return object;
    }
}
