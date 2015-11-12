package st.ilu.rms4csw.user;

import org.springframework.beans.factory.annotation.Autowired;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.UserRepository;

/**
 * @author Mischa Holz
 */
//@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOne(String id) {
        return userRepository.findOne(id);
    }
}
