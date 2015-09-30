package um;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by don on 9/28/15.
 */
public class UserResourceValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return UserResource.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        ValidationUtils.rejectIfEmpty(errors, "emailAddress", "emailAddress.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty");

        UserResource p = (UserResource) target;

        //TODO: perform additional checks if name already exists, etc.
    }
}
