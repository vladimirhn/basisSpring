package jwtsecurity.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import jwtsecurity.user.User;
import jwtsecurity.user.UsersService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersService usersService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersService.findByLogin(username);
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
