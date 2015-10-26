package exceptions;

public class MissingElementException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MissingElementException(){
	}
	
	public MissingElementException(String c){
		super("You are missing a " + c.toString());
	}
}