package st.ilu.rms4csw.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.security.token.Token;

import java.util.Collection;

/**
 * @author Mischa Holz
 */
public class UserAuthentication implements Authentication {

    private User user;

    private Token token;

    public UserAuthentication(User user, Token token) {
        this.user = user;
        this.token = token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user.getId();
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return user.getLoginName();
    }

    public User getUser() {
        return user;
    }

    public Token getToken() {
        return token;
    }
}
