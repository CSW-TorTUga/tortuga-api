package st.ilu.rms4csw.controller.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class UnauthorizedException extends RestException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
