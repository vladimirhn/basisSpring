package jwtsecurity.user;

import jwtsecurity.userdetails.CustomUserDetails;
import kpersistence.v2.CurrentUserIdProvider;
import kpersistence.v1.queryGeneration.QueryGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import repository.v1.AbstractTableRepository;
import repository.v2.AbstractStringIdTableRepository;

@Component
public class SpringApplicationUserProvider implements CurrentUserIdProvider {

    public SpringApplicationUserProvider() {
        QueryGenerator.currentUserIdProvider = this; //v1
        AbstractTableRepository.currentUserIdProvider = this; //v1
        AbstractStringIdTableRepository.currentUserIdProvider = this; //v2
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
