package st.ilu.rms4csw.controller.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class RestException extends RuntimeException {

    private HttpStatus status;

    public RestException(HttpStatus status) {
    }

    public RestException(HttpStatus status, String message) {
        super(message);
    }

    public RestException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
    }

    public RestException(HttpStatus status, Throwable cause) {
        super(cause);
    }

    public RestException(HttpStatus status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
