package um;

import java.util.List;

/**
 * Created by don on 9/28/15.
 */
public interface UserService {

    User create(User user);

    User delete(String emailAddress);

    List<User> findAll();

    User findById(String emailAdress);

    User update(User user);

    User login(String emailAddress, String password);
}
