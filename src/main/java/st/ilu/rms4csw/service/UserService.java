package st.ilu.rms4csw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;

import java.util.Date;

/**
 * @author Mischa Holz
 */
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByLoginName(s);
        if(user == null) {
            throw new UsernameNotFoundException("Did not find user with username " + s);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getLoginName(),
                user.getPassword(),
                (!user.getExpires().isPresent()) || user.getExpires().get().after(new Date()),
                true,
                true,
                true,
                user.getRole().getAuthorities()
        );
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
