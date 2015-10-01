package um;

import domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by don on 9/28/15.
 */
public interface HsqlUserRepository extends CrudRepository<User, String> {
    public List<User> findByName(String name);

    public User findById(String emailAddress);

}
