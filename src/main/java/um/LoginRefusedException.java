package um;

/**
 * Created by don on 9/28/15.
 */
public class LoginRefusedException extends AbstractServiceException {
    private InvalidLoginEnum reason;
    public LoginRefusedException(String message, InvalidLoginEnum reason) {
        super(message);
        this.reason = reason;
    }
}
