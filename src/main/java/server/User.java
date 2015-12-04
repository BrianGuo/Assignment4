package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;

/**
 * Created by Max on 11/26/2015.
 */
public class User {
    static int last_id = 0;
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

    public User(String level, String password){
        initID();
        this.level = level;
        this.password = password;
    }

    public User(){
        initID();
    }
    public void initID(){
        session_id = last_id;
        last_id++;
    }

}
