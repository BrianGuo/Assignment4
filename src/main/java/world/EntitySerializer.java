package world;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class EntitySerializer implements JsonSerializer<Entity>{

    @Override
    public JsonElement serialize(Entity entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.add("type", new JsonPrimitive(entity.getClass().getSimpleName().toLowerCase()));
        object.addProperty("row", entity.location.getRow());
        object.addProperty("col", entity.location.getCol());

        JsonObject result;
        if(entity instanceof Critter){
            result = new CritterSerializer().serialize((Critter) entity,
                    Critter.class, jsonSerializationContext)
                    .getAsJsonObject();
        }
        else {
            result = jsonSerializationContext.serialize(entity, entity.getClass()).getAsJsonObject();
        }
        for(Map.Entry<String, JsonElement> entry: result.entrySet()) {
            object.add(entry.getKey(), entry.getValue());
        }


        return object;
    }




}
