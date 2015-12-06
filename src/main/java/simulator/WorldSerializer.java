package simulator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import world.World;

import java.lang.reflect.Type;

/**
 * Created by ball9 on 12/5/2015.
 */
public class WorldSerializer implements JsonSerializer<Simulator>{
    //only serializes the basic parts of the world
    @Override
    public JsonElement serialize(Simulator simulator, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        String name = simulator.world.name;
        int rows = simulator.world.getRows();
        int cols = simulator.world.getColumns();
        int population = simulator.getNumCritters();

        object.addProperty("name", name);
        object.addProperty("rows", rows);
        object.addProperty("cols", cols);
        object.addProperty("population", population);

        return object;
    }
}
