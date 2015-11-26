package server;

import static spark.Spark.*;
/**
 * Created by Max on 11/26/2015.
 */
public class Server {

        public static void main(String[] args) {
            get("/hello", (req, res) -> "Hello World");
        }
}
