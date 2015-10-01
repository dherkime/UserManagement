package exceptions;

/**
 * Created by don on 9/28/15.
 */
public class AbstractServiceException extends RuntimeException{
    public AbstractServiceException(String message) {
        super(message);
    }

    public AbstractServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
