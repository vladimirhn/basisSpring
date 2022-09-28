package jwtsecurity.user;

import org.springframework.stereotype.Repository;
import repository.v2.AbstractStringIdTableRepository;

@Repository
public class UsersRepository extends AbstractStringIdTableRepository<User> {

    public UsersRepository() {
        super(User.class);
    }
}
