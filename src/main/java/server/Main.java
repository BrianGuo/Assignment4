package server;

import static spark.Spark.*;
import com.google.gson.*;
public class Main {

        public static void main(String[] args) {
            Gson gson = new Gson();


            get("/hello", (request, response) -> "Hello World");

            //Done
            post("/login", (request, response) ->{
                User user = gson.fromJson(request.body(), User.class);
                return gson.toJson(user);
            });
        }


}
