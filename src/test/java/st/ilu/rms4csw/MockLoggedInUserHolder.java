package st.ilu.rms4csw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;

/**
 * @author Mischa Holz
 */
@Service
public class MockLoggedInUserHolder implements LoggedInUserHolder {

    private User user = TestHelper.createLoginUser();

    private UserRepository userRepository;

    @Override
    public User getLoggedInUser() {
        return userRepository.findOne(user.getId());
    }

    public void setUp() {
        userRepository.deleteAllInBatch();

        user = userRepository.save(user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
