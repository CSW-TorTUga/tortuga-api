package st.ilu.rms4csw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.UserRepository;
import st.ilu.rms4csw.security.json.LoginRequest;
import st.ilu.rms4csw.util.ErrorResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mischa Holz
 */
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private UserRepository userRepository;

    private TokenAuthenticationService tokenAuthenticationService;

    public StatelessLoginFilter(String urlMapping, UserRepository userRepository, TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping));

        this.userRepository = userRepository;
        this.tokenAuthenticationService = tokenAuthenticationService;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if(!HttpMethod.POST.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

            ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "You need to post login information");
            new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), errorResponse);

            return null;
        }

        LoginRequest loginRequest = new ObjectMapper().readValue(httpServletRequest.getInputStream(), LoginRequest.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getLoginName(), loginRequest.getPassword());

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = userRepository.findOneByLoginName(authResult.getName());
        UserAuthentication userAuthentication = new UserAuthentication(user);

        tokenAuthenticationService.addAuthentication(response, user);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }


}
