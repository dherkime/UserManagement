package domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by don on 9/28/15.
 */
@ApiModel(description = "The user with a name, email address as the ID, a last login date, and a password")
public class UserResource extends ResourceSupport {
    private static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private String emailAddress;
    private String name;
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date lastLogin;


    public UserResource() {
    }

    @JsonCreator
    public UserResource(@JsonProperty("emailAddress") String emailAddress,
                        @JsonProperty("name") String name,
                        @JsonProperty("password") String password,
                        @JsonProperty("lastLogin") Date lastLogin) {
        this.emailAddress = emailAddress;
        this.name = name;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public static UserResource from(User user) {
        return new UserResource(user.getEmailAddress(), user.getName(), user.getPassword(), user.getLastLogin());
    }

    public static List<UserResource> from(List<User> users) {
        return users.stream()
                .map(user -> from(user))
                .collect(Collectors.toList());
    }

    public String getLastLogin() {
        if (null == lastLogin) {
            return "";
        }
        return SIMPLEDATEFORMAT.format(lastLogin);
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser() {
        return new User(this.name, this.emailAddress, this.password);
    }
}

