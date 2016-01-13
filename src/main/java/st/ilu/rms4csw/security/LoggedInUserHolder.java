package st.ilu.rms4csw.security;

import st.ilu.rms4csw.model.user.User;

/**
 * @author Mischa Holz
 */
public interface LoggedInUserHolder {

    User getLoggedInUser();

}
