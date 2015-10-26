package exceptions;

/**
 * An exception that signifies an illegal coordinate (NOT an OOB coordinate)
 */
public class IllegalCoordinateException extends Exception {
    public IllegalCoordinateException(String message){
        super(message);
    }
}
