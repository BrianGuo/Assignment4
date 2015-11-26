package server;

/**
 * Created by Max on 11/26/2015.
 */
public class User {
    public int getSession_id() {
        return session_id;
    }

    public String getLevel() {
        return level;
    }

    public String getPassword() {
        return password;
    }

    private int session_id = 0;
    private String level;
    private String password;

    public User(int session_id, String level, String password){
        this.session_id = session_id;
        this.level = level;
        this.password = password;
    }


}
