package server;

import world.Critter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ball9 on 12/2/2015.
 */
public class Security {
    /**
     * Map of all the levels and their passwords
     * Map<level, password>
     */
    static final Map<String, String> levels = new HashMap<>();

    /**
     * Authenticates a user with an entered password for a given security level.
     * @param level Level to log in at.  One of "read", "write", "admin".
     * @param password The provided password
     * @return true if password matches, false otherwise
     */
    public static boolean authenticate(String level, String password){
        if(!levels.containsKey(level)){
            return false;
        }
        return levels.get(level).equals(password);
    }

    /**
     * Checks that a user has the authorization of a given level.
     * Precondition: user is well-formed and actually has a level
     * @param user The user to authorize
     * @param level The minimum level required
     * @return true if authorized, false otherwise
     */
    public static boolean authorize(User user, String level){
        String userLevel = user.getLevel();
        switch(level){
            case "admin":
                return userLevel.equals("admin");
            case "write":
                return userLevel.equals("admin") || userLevel.equals("write");
            case "read":
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks that a user has access to a critter.
     * Returns true if user is an admin or the critter was created by the user.
     * @param user User requesting access
     * @param critter Critter to be accessed
     * @return true if authorized, false otherwise
     */
    public static boolean authorize(User user, Critter critter){
        return user.getLevel().equals("admin") || critter.creator == user.getSession_id();
    }

    //read from web.xml
    public static void init(){
        //TODO: INTEGRATE WITH STARTUP
        ArrayList<String> passwords = new ArrayList<>();
        try {
            BufferedReader f = new BufferedReader(new FileReader("passwords.txt"));
            levels.put("admin", f.readLine());
            levels.put("write", f.readLine());
            levels.put("read", f.readLine());
            System.out.println("Initialized passwords to those in passwords.txt.");
            System.out.println("admin:" + levels.get("admin"));
            System.out.println("write:" + levels.get("write"));
            System.out.println("read:" + levels.get("read"));
        }
        catch(IOException e){
            levels.clear();
            levels.put("admin", "admin");
            levels.put("write", "read");
            levels.put("read", "write");
            System.out.println("Initialized passwords to default");
            System.out.println("admin:" + levels.get("admin"));
            System.out.println("write:" + levels.get("write"));
            System.out.println("read:" + levels.get("read"));

        }

    }

}
