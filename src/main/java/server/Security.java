package server;

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
     * @return true if authorized, false othwerise
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

    //read from web.xml
    public static void init(){
        //TODO: INTEGRATE WITH STARTUP
        levels.put("admin", "admin");
        levels.put("read", "read");
        levels.put("write", "write");

    }

}
