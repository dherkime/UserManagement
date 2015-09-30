package um;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * Created by don on 9/28/15.
 */
@Document(collection="users")
public class User {

    @Id
    private String id;

    @NotNull
    @Min(4)
    @Max(100)
    private String name;

    @NotNull
    @Min(8)
    @Max(100)
    private String password;

    private Date lastLogin;

    private String emailAddress;

    @JsonCreator
    public User(@JsonProperty("name") String name,
                @JsonProperty("emailAddress") String emailAddress,
                @JsonProperty("password") String password) {
        this.name = name;
        this.id = emailAddress;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return id;
    }

    public void setEmailAddress(String emailAddress) {
        this.id = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, name='%s', lastLogin='%tF']",
                id, name, lastLogin);
    }
}
