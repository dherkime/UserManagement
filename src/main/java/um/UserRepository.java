package um;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by don on 9/28/15.
 */
public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByName(String name);

    public User findById(String emailAddress);

}
