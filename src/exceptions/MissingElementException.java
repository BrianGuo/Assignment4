package exceptions;

	
	
/**
 * Created by ball9 on 10/26/2015.
 */
public class MissingElementException extends Exception {
    public MissingElementException(){
        super();
    }
    public MissingElementException(String msg){
        super(msg);
    }
}
