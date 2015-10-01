package exceptions;

import domain.User;

/**
 * Created by don on 9/28/15.
 */
public class UserEmailAddressInUseException extends AbstractServiceException {

    private final String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public UserEmailAddressInUseException(User user) {
        super("The email address is already taken.");
        this.emailAddress = user.getEmailAddress();
    }
}
