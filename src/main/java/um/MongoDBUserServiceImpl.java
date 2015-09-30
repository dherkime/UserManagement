package um;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by don on 9/28/15.
 */
@Profile("mongodb")
@Service
public class MongoDBUserServiceImpl implements UserService
{
    private final UserRepository repository;

    @Autowired
    MongoDBUserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User delete(String emailAddress) {
        final User deleted = findById(emailAddress);
        if (null != deleted) {
            repository.delete(emailAddress);
        }
        return deleted;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(String emailAddress) {
        return repository.findById(emailAddress);
    }

    @Override
    public User update(User user) {
        final User updated = findById(user.getEmailAddress());
        if (null == updated) {
            //FIXME: throw UserNotFoundException or some such
        }
        BeanUtils.copyProperties(user, updated);
        return repository.save(updated);
    }

    @Override
    public User login(String emailAddress, String password) {
        final User user = findById(emailAddress);
        if (null == user) {
            throw new LoginRefusedException("Invalid email address or password", InvalidLoginEnum.InvalidEmailAddress);
        }

        if (!user.getPassword().equals(password)) {
            throw new LoginRefusedException("Invalid email address or password", InvalidLoginEnum.InvalidPassword);
        }

        return user;
    }
}
