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
import st.ilu.rms4csw.controller.base.advice.RestExceptionHandler;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.security.json.LoginRequest;
import st.ilu.rms4csw.security.token.Token;
import st.ilu.rms4csw.util.NetworkUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {

    private UserRepository userRepository;

    private TokenAuthenticationService tokenAuthenticationService;

    private ObjectMapper objectMapper;

    public StatelessLoginFilter(String urlMapping, UserRepository userRepository, TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authManager, ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(urlMapping));

        this.userRepository = userRepository;
        this.tokenAuthenticationService = tokenAuthenticationService;

        this.objectMapper = objectMapper;

        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if(!HttpMethod.POST.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());

            RestExceptionHandler.ErrorResponse errorResponse = new RestExceptionHandler.ErrorResponse(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "You need to post login information");
            objectMapper.writeValue(httpServletResponse.getOutputStream(), errorResponse);

            return null;
        }


        LoginRequest loginRequest = objectMapper.readValue(httpServletRequest.getInputStream(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(loginRequest.getLoginName(), loginRequest.getPassword());

        authResult.setDetails(loginRequest);

        return getAuthenticationManager().authenticate(authResult);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        boolean longToken;
        if(authResult.getDetails() instanceof LoginRequest) {
            longToken = (!NetworkUtil.isLocalNetworkRequest(request)) && ((LoginRequest) authResult.getDetails()).getLongToken();
        } else {
            throw new AssertionError("Couldn't get the LoginRequest");
        }

        User user = userRepository.findOneByLoginName(authResult.getName());

        Token token = tokenAuthenticationService.addAuthentication(response, user, longToken, Optional.empty());

        UserAuthentication userAuthentication = new UserAuthentication(user, token);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);

        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), user);
    }


}
