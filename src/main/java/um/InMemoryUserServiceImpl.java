package um;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by don on 9/28/15.
 */
@Profile("default")
@Service
public class InMemoryUserServiceImpl extends AbstractUserServiceImpl {

    @Autowired
    InMemoryUserServiceImpl(HsqlUserRepository repository) {
        super(repository);
    }

}
