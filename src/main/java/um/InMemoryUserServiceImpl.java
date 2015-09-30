package um;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by don on 9/28/15.
 */
@Profile("default")
@Service
public class InMemoryUserServiceImpl implements UserService {
    private Map<String, User> users = new ConcurrentHashMap<String, User>();

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserEmailAddressInUseException(user);
        }

        final User newUser = new User(user.getName(), user.getEmailAddress(), user.getPassword());
        users.put(newUser.getEmailAddress(), newUser);
        return newUser;
    }

    @Override
    public User delete(String emailAddress) {
        if (!users.containsKey(emailAddress)) {
            return null;
        }

        User user = users.get(emailAddress);
        users.remove(emailAddress);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public User findById(String emailAddress) {
        return users.get(emailAddress);
    }

    @Override
    public User update(User user) {
        final String emailAddress = user.getEmailAddress();
        if (users.containsKey(emailAddress)) {
            final User updatedUser = users.get(emailAddress);
            BeanUtils.copyProperties(user, updatedUser);
        }

        return null;
    }

    @Override
    public User login(String emailAddress, String password) {
        if (!users.containsKey(emailAddress)) {
            throw new LoginRefusedException("Invalid email address or password", InvalidLoginEnum.InvalidEmailAddress);
        }

        final User user = users.get(emailAddress);
        if (!user.getPassword().equals(password)) {
            throw new LoginRefusedException("Invalid email address or password", InvalidLoginEnum.InvalidPassword);
        }

        return user;
    }
}
