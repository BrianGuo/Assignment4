package server;

import static spark.Spark.*;
import com.google.gson.*;
import simulator.Simulator;
import world.Critter;
import world.CritterSerializer;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    /**
     * HashMap of users, with the key being the user's session_id.
     */
    static HashMap<Integer, User> users = new HashMap<>();

    static Simulator sim = new Simulator();
    static final Timer timer = new Timer();

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

        get("/hello", (request, response) -> "Hello World");

        //Done
        post("/login", (request, response) ->{
            Gson userGson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer())
                    .setPrettyPrinting().create();
            User user = userGson.fromJson(request.body(), User.class);
            //return user.getLevel() + user.getPassword();


            if(Security.authenticate(user.getLevel(), user.getPassword())) {
                users.put(user.getSession_id(), user);
                response.status(200);
                return userGson.toJson(user);
            }
            else{
                halt(401, "Could not login with that level/password combination.");
                return "Unauthorized."; //doesn't make it here
            }

        });

        get("/critters", (request, response) -> {
            Gson critterGson = new GsonBuilder().registerTypeAdapter(Critter.class, new CritterSerializer())
                    .setPrettyPrinting().create();
            //JsonObject result = new JsonObject();
            JsonArray critterArray = new JsonArray();
            //result.add(critterArray);
            int session_id;
            try {
                session_id = Integer.parseInt(request.queryParams("session_id"));
            }
            catch (NumberFormatException e){
                halt(401, "Illegal session_id"); //ex.  session_id of "Q"
                return "Unauthorized";
            }

            User user = users.get(session_id);
            if(user == null)
                halt(401, "Invalid session_id"); //ex. session_id of "-1", which will never happen

            for(Critter c: sim.world.getCritters().values()){
                JsonElement cJ = critterGson.toJsonTree(c); //JsonElement of the critter thing
                JsonObject cJo = cJ.getAsJsonObject();
                if(!Security.authorize(user, c)){
                    cJo.remove("recently_executed_rule");
                }
                critterArray.add(cJo);
            }
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
            return session_id;
        });

        post("/world", (request, response) -> {
            String worldDef = gson.fromJson(request.body(), String.class);
            return worldDef;
            //return "hi";
        });

    }


}
