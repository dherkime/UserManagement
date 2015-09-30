package um;

/**
 * Created by don on 9/29/15.
 */
public class UserNotFoundException extends AbstractServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
