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
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = null;
        try {
            user = usersService.findById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found");
        }
        return CustomUserDetails.fromUserToCustomUserDetails(user);
    }
}
