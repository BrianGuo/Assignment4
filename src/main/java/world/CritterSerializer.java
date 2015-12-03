package world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by ball9 on 12/3/2015.
 */
public class CritterSerializer implements JsonSerializer<Critter> {
    @Override
    public JsonElement serialize(Critter critter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        String id = String.valueOf(critter.getId());
        String creator = String.valueOf(critter.creator);
        String species_id = critter.getSpecies().replace(' ', '_'); //sanitize spaces
        String program = critter.getProgram().toString();
        String row = String.valueOf(critter.getLocation().getRow());
        String col = String.valueOf(critter.getLocation().getCol());
        String memory = Arrays.toString(critter.attributes);
        String recently_executed_rule = "3"; //TODO: replace with actual one

        object.addProperty("id", id);
        object.addProperty("creator", creator);
        object.addProperty("species_id", species_id);
        object.addProperty("program", program);
        object.addProperty("row", row);
        object.addProperty("col", col);
        object.addProperty("memory", memory);
        object.addProperty("recently_executed_rule", recently_executed_rule);
        return object;
    }
}
