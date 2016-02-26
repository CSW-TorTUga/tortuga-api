package st.ilu.rms4csw.controller.base.response;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class UnauthorizedResponse extends RestResponse {
    public UnauthorizedResponse() {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
