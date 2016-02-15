package st.ilu.rms4csw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Service
public class SpringSecurityLoggedInUserHolder implements LoggedInUserHolder {

    private UserRepository userRepository;

    public SpringSecurityLoggedInUserHolder() {
    }

    @Override
    public Optional<User> getLoggedInUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null) {
            return Optional.empty();
        }

        Authentication authentication = context.getAuthentication();
        if(authentication == null) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal == null) {
            return Optional.empty();
        }

        if(principal instanceof String) {
            return Optional.ofNullable(userRepository.findOne((String) principal));
        } else if(principal instanceof User) {
            return Optional.of((User) principal);
        } else {
            throw new RuntimeException("I don't know what to do with this principal: " + principal);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
