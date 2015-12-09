package st.ilu.rms4csw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.token.Token;
import st.ilu.rms4csw.security.token.TokenException;
import st.ilu.rms4csw.security.token.TokenHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author Mischa Holz
 */
@Service
public class TokenAuthenticationService {

    private static final String COOKIE_NAME = "auth_token";

    private TokenHandler tokenHandler;

    private long validFor;

    private UserRepository userRepository;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secret, @Value("${token.validFor}") Long validFor) {
        this.tokenHandler = new TokenHandler(secret.getBytes());
        this.validFor = validFor;
    }

    public void addAuthentication(HttpServletResponse response, User user) {
        String strToken = tokenHandler.createTokenForUser(user, validFor);

        Cookie cookie = new Cookie(COOKIE_NAME, strToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (this.validFor / 1000));
        response.addCookie(cookie);

        response.setHeader("X-Next-Auth-Token", strToken);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String strToken = Arrays
                .stream(request.getCookies() == null ? new Cookie[0] : request.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findAny()
                .orElseGet(() -> {
                    String value = request.getHeader(HttpHeaders.AUTHORIZATION);
                    if(value == null || value.isEmpty()) {
                        return null;
                    }
                    return value;
                });
        if(strToken == null) {
            throw new TokenException("No token in either the cookie " + COOKIE_NAME + " or the header " + HttpHeaders.AUTHORIZATION);
        }

        Token token = tokenHandler.validateToken(strToken);

        User user = userRepository.findOne(token.getId());
        if(user == null) {
            throw new TokenException("Can't find user with given id");
        }

        return new UserAuthentication(user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
