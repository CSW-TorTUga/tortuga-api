package st.ilu.rms4csw.controller.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class NotFoundException extends RestException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, cause);
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(HttpStatus.NOT_FOUND, message, cause, enableSuppression, writableStackTrace);
    }
}
