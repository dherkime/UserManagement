package um;

import com.google.common.collect.Lists;
import domain.User;
import exceptions.InvalidLoginEnum;
import exceptions.LoginRefusedException;
import exceptions.UserEmailAddressInUseException;
import exceptions.UserNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by don on 9/30/15.
 */
public class AbstractUserServiceImpl implements UserService {

    private CrudRepository<User, String> repository;

    @Autowired
    AbstractUserServiceImpl(CrudRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        try {
            findById(user.getEmailAddress());
        } catch (UserNotFoundException e) {
            return repository.save(user);
        }
        throw new UserEmailAddressInUseException(user);
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
        return Lists.newArrayList(repository.findAll());
    }

    @Override
    public User findById(String emailAddress) {
        User user = repository.findOne(emailAddress);
        if (null == user) {
            throw new UserNotFoundException("There was no user with email address = \"" + emailAddress + "\"");
        }
        return user;
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

        user.setLastLogin(new Date());
        update(user);
        return user;
    }
}
