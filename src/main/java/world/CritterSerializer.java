package world;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by ball9 on 12/3/2015.
 */
public class CritterSerializer implements JsonSerializer<Critter> {
    @Override
    public JsonElement serialize(Critter critter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        int id = critter.getId();
        String creator = String.valueOf(critter.creator);
        String species_id = critter.getSpecies().replace(' ', '_'); //sanitize spaces
        String program = critter.getProgram().toString();

        int row = critter.getLocation().getRow();
        int col = critter.getLocation().getCol();
        int[] memory = critter.attributes;
        int recently_executed_rule = critter.recentlyExecutedRule;
        int direction = critter.getDirection();

        object.addProperty("id", id);
        //object.addProperty("creator", creator);
        object.addProperty("species_id", species_id);
        object.add("program", new JsonPrimitive(program));
        //object.addProperty("program", program);
        object.addProperty("row", row);
        object.addProperty("col", col);

        JsonArray e = new JsonArray();
        for(int i: memory){
            e.add(i);
        }
        object.add("mem", e);
        object.addProperty("recently_executed_rule", recently_executed_rule);
        object.addProperty("direction", direction);
        return object;
    }
}
