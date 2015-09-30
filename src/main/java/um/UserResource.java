package um;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by don on 9/28/15.
 */
public class UserResource extends ResourceSupport {

    private String emailAddress;

    private String name;

    private String password;

    public UserResource() {
    }

    @JsonCreator
    public UserResource(@JsonProperty("emailAddress") String emailAddress,
                        @JsonProperty("name") String name,
                        @JsonProperty("password") String password) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.password = password;
    }


    public User toUser() {
        return new User(this.name, this.emailAddress, this.password);
    }

    public static UserResource from(User user) {
        return new UserResource(user.getEmailAddress(), user.getName(), user.getPassword());
    }

    public static List<UserResource> from(List<User> users) {
        return users.stream()
                .map(user -> from(user))
                .collect(Collectors.toList());
    }
}

