package st.ilu.rms4csw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    private long longValidFor;

    private long shortValidFor;

    private UserRepository userRepository;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secret, @Value("${token.longValidFor}") Long longValidFor, @Value("${token.shortValidFor}") Long shortValidFor) {
        this.tokenHandler = new TokenHandler(secret.getBytes());
        this.longValidFor = longValidFor;
        this.shortValidFor = shortValidFor;
    }

    public Token addAuthentication(HttpServletResponse response, User user, boolean longToken, Optional<Token> oldToken) {
        Token token;

        if(oldToken.isPresent()) {
            token = tokenHandler.createTokenForUser(user, oldToken.get().getValidFor());
        } else {
            if(longToken) {
                token = tokenHandler.createTokenForUser(user, longValidFor);
            } else {
                token = tokenHandler.createTokenForUser(user, shortValidFor);
            }
        }

        String strToken = tokenHandler.signToken(token);
        try {
            strToken = URLEncoder.encode(strToken, "UTF-8");
        } catch(UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }

        Cookie cookie = new Cookie(COOKIE_NAME, strToken);
        cookie.setPath("/");

        if(oldToken.isPresent()) {
            cookie.setMaxAge((int) (oldToken.get().getValidFor() / 1000));
        } else {
            if(longToken) {
                cookie.setMaxAge((int) (longValidFor / 1000));
            } else {
                cookie.setMaxAge((int) (shortValidFor / 1000));
            }
        }

        response.addCookie(cookie);

        response.setHeader("X-Next-Auth-Token", strToken);

        return token;
    }

    public Optional<UserAuthentication> getAuthentication(HttpServletRequest request) {
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
            return Optional.of(new UserAuthentication(user, token));
        }

        return Optional.empty();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
