package server;

import static spark.Spark.*;
import com.google.gson.*;
//import com.google.gson.
import exceptions.SyntaxError;
import interpret.CritterInterpreter;
import simulator.Simulator;
import simulator.WorldSerializer;
import spark.Request;
import spark.Response;
import world.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    /**
     * HashMap of users, with the key being the user's session_id.
     */
    static HashMap<Integer, User> users = new HashMap<>();

    static Simulator sim = new Simulator();
    static Timer timer = new Timer();
    static private double rate;


    static private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static private final Lock readLock = readWriteLock.readLock();
    static private final Lock writeLock = readWriteLock.writeLock();
    //probably should have just put locks in here to begin with...

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
            Gson critterGson = new GsonBuilder().registerTypeAdapter(Entity.class, new EntitySerializer())
                    .setPrettyPrinting().create();
            //JsonObject result = new JsonObject();
            JsonArray critterArray = new JsonArray();
            //result.add(critterArray);
            User user = authenticate(request);
            for(Critter c: sim.world.getCritters().values()){
                JsonObject cJo = critterGson.toJsonTree(c).getAsJsonObject(); //JsonElement of the critter thing
                censorCritter(user, c, cJo);
                critterArray.add(cJo);
            }
            response.type("application/json");
            response.status(201);
            return critterArray;
        });

        get("/*/critter", (request, response) -> {
            User user = authenticate(request);
            int id;
            try {
                System.out.println(request.queryParams("id"));
                id = Integer.parseInt(request.queryParams("id"));
            }
            catch(NumberFormatException e){
                halt(400, "Invalid id provided");
                return "no";
            }
            Critter wanted = sim.world.getCritters().get(id);

            Gson critterGson = new GsonBuilder().registerTypeAdapter(Critter.class, new CritterSerializer())
                    .setPrettyPrinting().create();
            response.status(200);
            response.type("application/json");
            
            if(wanted == null){
                return gson.toJson(new JsonObject());
            }
            JsonObject cJo = critterGson.toJsonTree(wanted, Critter.class).getAsJsonObject();
            censorCritter(user, wanted, cJo);

            return gson.toJson(cJo);
        });

        post("/*/world", (request, response) -> {
            User user = authenticate(request);
            if(!Security.authorize(user, "admin")){
                halt(401, "Only an admin can create a new world");
                return "no";
            }
            WorldDef worldDef = gson.fromJson(request.body(), WorldDef.class);
            if(worldDef == null || worldDef.description == null){
                halt(400, "Invalid JSON format");
                return "null";
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

        //when update_since is negative, the entire world is returned.
        //entire world is also returned unless all 4 bounds provided
        get("/*/world", (request, response) -> {
            readLock.lock();
            try {
                User user = authenticate(request);
                int from_row, to_row, from_col, to_col;
                Set<String> qP = request.queryParams();

                if (qP.contains("from_row") && qP.contains("to_row") && qP.contains("from_col") && qP
                        .contains("to_col")) {

                    try {
                        from_row = Integer.parseInt(request.queryParams("from_row"));
                        to_row = Integer.parseInt(request.queryParams("to_row"));
                        from_col = Integer.parseInt(request.queryParams("from_col"));
                        to_col = Integer.parseInt(request.queryParams("to_col"));
                    } catch (NumberFormatException e) {
                        halt(400, "Invalid bounds provided");
                        return "Invalid";
                    }
                    return handleWorldStatus(gson, request, response, user, from_row, to_row, from_col, to_col);
                }
                else {
                    return handleWorldStatus(gson, request, response, user, 0, sim.getWorldRows(), 0, sim.getWorldColumns());
                }
            }
            finally{
                readLock.unlock();
            }

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
            if (count < 0)
                halt(406, "Not acceptable--negative count");
            System.out.println(count);

            //TODO: need synchronization?
            sim.advance(count);
            response.type("text/plain");

            return "Ok";
        });

        post("/*/run", (request, response) -> {
            User user = authenticate(request);
            if (!Security.authorize(user, "write")) {
                halt(401, "Unauthorized");
            }
            Map<String, Double> m = gson.fromJson(request.body(), Map.class);
            double simRate;
            if (m != null && m.containsKey("rate")) {
                simRate = m.get("rate");
                if (simRate < 0) {
                    halt(406);
                }
            } else {
                halt(406, "No rate specified");
                return "invalid";
            }

            rate = simRate;

            timer.cancel();
            if (rate > 0) {
                timer = new Timer();

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        sim.advance(1);
                    }
                }, 0, (int) ((1 / rate) * 1000));
            }
            response.type("text/plain");
            return "Ok";
        });


    }

    private static Object handleWorldStatus(Gson gson, Request request, Response response, User user,
                                            int from_row, int to_row, int from_col, int to_col) {
        Gson worldGson = new GsonBuilder().registerTypeAdapter(Simulator.class, new WorldSerializer())
                .setPrettyPrinting().create();
        JsonObject root = worldGson.toJsonTree(sim).getAsJsonObject();
        root.addProperty("rate", rate);

        int update_since;
        try{
            update_since = Integer.parseInt(request.queryParams("update_since"));
            root.addProperty("update_since", Integer.parseInt(request.queryParams("update_since")));
        }
        catch(NumberFormatException e){
            update_since = -1;
        }
        //if(update_since < 0) update_since = 0;


        Gson entityGson = new GsonBuilder().registerTypeAdapter(Entity.class, new EntitySerializer())
                .setPrettyPrinting().disableHtmlEscaping().create();

        JsonArray critterArray = new JsonArray();
        //result.add(critterArray);

        //if update_since is not given, return ENTIRE world without diffing

        if(update_since < 0){
            for(int i = from_col; i <= to_col; i++){
                for(int j = from_row; j <= to_row; j++){
                    if(sim.world.inBounds(i,j)){
                        Entity e = sim.getEntityAt(i,j);
                        JsonObject cJo = getCoordAsJson(user, entityGson, i, j, e);
                        critterArray.add(cJo);
                    }
                }
            }
            root.add("dead_critters", gson.toJsonTree(sim.getObituaries(0).toArray()));
        }
        else {
            for (Coordinate c : sim.getDiffs(update_since)) {
                if(c.getRow() >= from_row && c.getRow() <= to_row &&
                        c.getCol() >= from_col && c.getCol() <= to_col) {

                    Entity e = sim.getEntityAt(c);
                    JsonObject cJo = getCoordAsJson(user, entityGson, c.getCol(), c.getRow(), e);
                    critterArray.add(cJo);
                }
            }
            root.add("dead_critters", gson.toJsonTree(sim.getObituaries(update_since).toArray()));
        }

        response.type("application/json");

        root.addProperty("current_version_number", sim.getCurrent_version_number());
        root.addProperty("current_timestep", sim.getTimesteps());
        root.add("state", critterArray);
        return root;
    }

    private static JsonObject getCoordAsJson(User user, Gson entityGson, int i, int j, Entity e) {
        if(e == null)
            e = new Nothing(i,j);
        JsonObject cJo = entityGson.toJsonTree(e, Entity.class).getAsJsonObject(); //JsonElement of the critter thing
        if (e instanceof Critter) {
            censorCritter(user, (Critter) e, cJo);
        }
        return cJo;
    }

    private static void censorCritter(User user, Critter c, JsonObject cJo) {
        if(!Security.authorize(user, c)){
            cJo.remove("recently_executed_rule");
            cJo.remove("program");
        }
    }

    /**
     * Looks at a request that contains a session_id and checks that it corresponds to a valid user.
     * Halts with 400 response if illegal ID (ex. a letter), 401 response if user not found
     * @param request request
     * @return The User logged in if valid
     */
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
