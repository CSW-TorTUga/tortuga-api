package st.ilu.rms4csw.controller.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class RestException extends RuntimeException {

    private HttpStatus status;

    public RestException(HttpStatus status) {
        this.status = status;
    }

    public RestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public RestException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public RestException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public RestException(HttpStatus status, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
