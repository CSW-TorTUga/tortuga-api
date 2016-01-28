package st.ilu.rms4csw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;

/**
 * @author Mischa Holz
 */
@Service
public class SpringSecurityLoggedInUserHolder implements LoggedInUserHolder {

    private UserRepository userRepository;

    public SpringSecurityLoggedInUserHolder() {
    }

    @Override
    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof String) {
            return userRepository.findOneByLoginName((String) principal);
        } else if(principal instanceof User) {
            return (User) principal;
        } else {
            throw new RuntimeException("I don't know what to do with this principal: " + principal);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
