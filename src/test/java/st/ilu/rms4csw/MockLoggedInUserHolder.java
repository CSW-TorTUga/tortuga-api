package st.ilu.rms4csw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Service
public class MockLoggedInUserHolder implements LoggedInUserHolder {

    private User user = TestHelper.createLoginUser();

    private UserRepository userRepository;

    @Override
    public Optional<User> getLoggedInUser() {
        if(user == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(userRepository.findOne(user.getId()));
    }

    public void forgetMe() {
        user = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUp() {
        userRepository.deleteAllInBatch();

        user = TestHelper.createLoginUser();

        user = userRepository.save(user);
    }

    public void tearDown() {
        if(user != null) {
            userRepository.delete(user);
        }

        userRepository.deleteAllInBatch();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
