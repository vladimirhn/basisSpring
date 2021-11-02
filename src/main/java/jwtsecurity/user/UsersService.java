package jwtsecurity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.AbstractTableRepository;
import service.AbstractTableService;


@Service
public class UsersService extends AbstractTableService<User> {

    @Autowired
    UsersRepository repository;
    
    @Override
    protected AbstractTableRepository<User> getRepository() {
        return repository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String newUserId = repository.insert(user);

        user.setId(newUserId);
        return user;
    }

    public User findByLogin(String login) {
        return repository
                .selectByField(User::setLogin, login)
                .getFirst()
                .orElse(null);
    }

    public User findByLoginAndPassword(String login, String password) {

        User user = findByLogin(login);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
