package exceptions;

/**
 * An exception indicating a syntax error.
 */
public class SyntaxError extends Exception {
    private static final long serialVersionUID = 211220140930L;
    public SyntaxError(String message){
        super(message);
    }
    
    public SyntaxError(){
    	super();
    }
}
