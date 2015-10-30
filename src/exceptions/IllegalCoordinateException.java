package exceptions;

/**
 * An exception that signifies an illegal coordinate (NOT an OOB coordinate)
 */
public class IllegalCoordinateException extends RuntimeException {
    public IllegalCoordinateException(String message){
        super(message);
    }
}
