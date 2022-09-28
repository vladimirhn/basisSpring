package jwtsecurity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import service.AbstractStringIdTableService;


@Service
public class UsersService extends AbstractStringIdTableService<User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String login, String password) {

        User user = new User(login, "ROLE_USER", passwordEncoder.encode(password));

        String newUserId = repository().insert(user);
        user.setId(newUserId);
        return user;
    }

    public boolean isLoginExists(String login) {
        return repository().selectByField(User::setLogin, login).isNotEmpty();
    }

    public User findByLogin(String login) {

        String sql = "SELECT id users__id, login users__login, role users__role, password users__password FROM users WHERE login = ?";
        Object[] params = new Object[]{login};

        User user = repository()
                .customSelect(sql, params)
                .getFirst()
                .orElse(null);

        return user;
    }

    public User findById(String id) {

        String sql = "SELECT id users__id, login users__login, role users__role, password users__password FROM users WHERE id = ?";
        Object[] params = new Object[]{id};

        User user = repository()
                .customSelect(sql, params)
                .getFirst()
                .orElse(null);

        return user;
    }

    public User findByLoginAndPassword(String login, String password) {

        User user = findByLogin(login);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
