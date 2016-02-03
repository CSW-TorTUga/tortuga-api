package st.ilu.rms4csw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.token.Token;
import st.ilu.rms4csw.security.token.TokenHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Optional;

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
        try {
            strToken = URLEncoder.encode(strToken, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }

        Cookie cookie = new Cookie(COOKIE_NAME, strToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (this.validFor / 1000));
        response.addCookie(cookie);

        response.setHeader("X-Next-Auth-Token", strToken);
    }

    public Optional<Authentication> getAuthentication(HttpServletRequest request) {
        String strToken = Arrays
                .stream(request.getCookies() == null ? new Cookie[0] : request.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .map(v -> {
                    try {
                        return URLDecoder.decode(v, "UTF-8");
                    } catch(UnsupportedEncodingException e) {
                        throw new AssertionError(e);
                    }
                })
                .findAny()
                .orElseGet(() -> {
                    String value = request.getHeader(HttpHeaders.AUTHORIZATION);
                    if(value == null || value.isEmpty()) {
                        return null;
                    }
                    return value;
                });
        if(strToken == null) {
            return Optional.empty();
        }

        Token token = tokenHandler.validateToken(strToken);

        User user = userRepository.findOne(token.getId());
        if(user == null) {
            return Optional.empty();
        }

        if(user.isActiveUser()) {
            return Optional.of(new UserAuthentication(user));
        }

        return Optional.empty();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
