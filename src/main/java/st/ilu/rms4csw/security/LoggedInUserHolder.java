package st.ilu.rms4csw.security;

import st.ilu.rms4csw.model.user.User;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
public interface LoggedInUserHolder {

    Optional<User> getLoggedInUser();

}
