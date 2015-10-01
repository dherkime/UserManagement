package um;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by don on 9/28/15.
 */
@Profile("mongodb")
@Service
public class MongoDBUserServiceImpl extends AbstractUserServiceImpl
{

    @Autowired
    MongoDBUserServiceImpl(MongoUserRepository repository) {
        super(repository);
    }

}
