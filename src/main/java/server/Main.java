package server;

import static spark.Spark.*;
import com.google.gson.*;
public class Main {

        public static void main(String[] args) {
            Gson gson = new Gson();


            get("/hello", (request, response) -> "Hello World");
            post("/login", (request, response) ->{
                User user = gson.fromJson(request.body(), User.class);
                return request.attributes() + "<br>hi!<br>" + request.body() + "<br>" + gson.toJson(user);
            });
        }


}
