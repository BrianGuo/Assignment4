package server;

import static spark.Spark.*;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;
import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import parse.ParserFactory;
import simulator.Simulator;
import world.Critter;
import world.CritterSerializer;
import world.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    /**
     * HashMap of users, with the key being the user's session_id.
     */
    static HashMap<Integer, User> users = new HashMap<>();

    static Simulator sim = new Simulator();
    static final Timer timer = new Timer();
    static private double rate;

    /**
     * Gets a logged-in user by session_id.
     * @param session_id session_id of the user to look up
     * @return User object of the user corresponding to the session_id, or null if none exists
     */
    private User getUser(int session_id){
        return users.get(session_id);
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        Security.init();
        sim = new Simulator(Factory.getRandomWorld(), new CritterInterpreter());

        get("/*/hello", (request, response) -> "Hello World");

        //Done
        post("/*/login", (request, response) ->{
            Gson userGson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer())
                    .setPrettyPrinting().create();
            User user;
            try {
                user = userGson.fromJson(request.body(), User.class);
            }
            catch(Exception e){ //java said no MalformedJsonException is thrown in try..when it is...
                halt(400, "Malformed JSON received.");
                return "bye";
            }
            //return user.getLevel() + user.getPassword();


            if(Security.authenticate(user.getLevel(), user.getPassword())) {
                users.put(user.getSession_id(), user);
                response.status(200);
                response.type("application/json");
                return userGson.toJson(user);
            }
            else{
                halt(401, "Could not login with that level/password combination.");
                return "Unauthorized."; //doesn't make it here
            }

        });

        get("/*/critters", (request, response) -> {
            Gson critterGson = new GsonBuilder().registerTypeAdapter(Critter.class, new CritterSerializer())
                    .setPrettyPrinting().create();
            //JsonObject result = new JsonObject();
            JsonArray critterArray = new JsonArray();
            //result.add(critterArray);
            User user = authenticate(request);
            for(Critter c: sim.world.getCritters().values()){
                JsonElement cJ = critterGson.toJsonTree(c); //JsonElement of the critter thing
                JsonObject cJo = cJ.getAsJsonObject();
                if(!Security.authorize(user, c)){
                    cJo.remove("recently_executed_rule");
                    cJo.remove("program");
                }
                critterArray.add(cJo);
            }
            response.type("application/json");
            response.status(201);


            //return critterArray;
            /*
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sim.advance(1);
                }
            }, 0, 1000);
*/
            //TODO: complete
            return "hi";
        });

        post("/*/world", (request, response) -> {
            WorldDef worldDef = gson.fromJson(request.body(), WorldDef.class);
            if(worldDef.description == null){
                halt(400, "Invalid JSON format");
            }
            try{
                sim.parseWorldString(worldDef.description);
            }
            catch(SyntaxError e){
                halt(400, "Invalid world file given");
                return "bye";
            }
            //TODO: finish this


            return worldDef.description;
            //return "hi";
        });

        get("/*/world", (request, response) -> {
            //TODO: FINISH
            return "hi";
        });


        post("/*/step", (request, response) -> {
            User user = authenticate(request);

            if (!Security.authorize(user, "write")) {
                halt(401, "Unauthorized");
            }
            if (rate != 0.0) {
                halt(406, "World currently running continuously");
            }
            Map<String, Double> m = gson.fromJson(request.body(), Map.class);
            int count;
            if (m!= null && m.containsKey("count")) {
                count = m.get("count").intValue();
            }
            else{
                count = 1;
            }
            if (count <= 0) count = 1;


            sim.advance(count);

            return sim.getTimesteps() + sim.getCurrent_version_number();
        });

    }


    private static User authenticate(spark.Request request) {
        int session_id = -1;
        try {
            session_id = Integer.parseInt(request.queryParams("session_id"));
        } catch (NumberFormatException e) {
            halt(400, "Illegal session_id");

        }
        User user = users.get(session_id);

        if (user == null) {
            halt(401, "User not found");
        }
        return user;
    }
}
