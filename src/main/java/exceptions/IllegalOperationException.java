package exceptions;

/**
 * Created by Max on 11/11/2015.
 */
public class IllegalOperationException extends RuntimeException{
    public IllegalOperationException(String s) {
        super(s);
    }
}
