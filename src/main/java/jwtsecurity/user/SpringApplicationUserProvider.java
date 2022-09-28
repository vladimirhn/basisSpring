package jwtsecurity.user;

import jwtsecurity.userdetails.CustomUserDetails;
import kpersistence.CurrentUserIdProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import repository.v2.AbstractStringIdTableRepository;

@Component
public class SpringApplicationUserProvider implements CurrentUserIdProvider {

    public SpringApplicationUserProvider() {
        AbstractStringIdTableRepository.currentUserIdProvider = this;
    }

    @Override
    public String getCurrentUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) return null;

        CustomUserDetails currentUserDetails = (CustomUserDetails) principal;

        if (currentUserDetails != null) {
            return currentUserDetails.getId();
        }

        return null;
    }
}
