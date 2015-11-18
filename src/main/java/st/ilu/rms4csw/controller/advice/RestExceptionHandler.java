package st.ilu.rms4csw.controller.advice;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import st.ilu.rms4csw.controller.exception.NotFoundException;
import st.ilu.rms4csw.controller.exception.RestException;
import st.ilu.rms4csw.util.ErrorResponse;
import st.ilu.rms4csw.util.ValidationError;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * @author Mischa Holz
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    private ResponseEntity<ErrorResponse> handleException(HttpStatus defaultStatus, String defaultMessage, Exception e) {

        LOGGER.warn("Handling REST exception: {} - {}", e.getClass().getSimpleName(), e.getMessage());

        HttpStatus errorCode = defaultStatus;
        String errorMessage = defaultMessage;

        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            errorCode = responseStatus.value();
            errorMessage = responseStatus.reason();
        }

        return new ResponseEntity<>(new ErrorResponse(errorCode.value(), errorMessage), errorCode);
    }

    @ExceptionHandler(RestException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> restExceptionHandler(Exception e) throws Exception {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception e) throws Exception {
        return handleException(HttpStatus.NOT_FOUND, "Resource not found", e);
    }


    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(Exception e) {
        return handleException(HttpStatus.BAD_REQUEST, "Bad Request", e);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception e) {
        return handleException(HttpStatus.BAD_REQUEST, "Bad Request", e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception e) {
        return handleException(HttpStatus.BAD_REQUEST, "Bad Request: " + e.getMessage(), e);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnsatisfiedServletRequestParameterException(UnsatisfiedServletRequestParameterException e) {
        String message = "Parameter(s) missing or wrong: ";
        String delim = "";
        for (String param : e.getParamConditions()) {
            message += delim + "'" + param + "'";
            delim = ", ";
        }
        return handleException(HttpStatus.BAD_REQUEST, message, e);
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(Exception e, HttpServletResponse response) {
        Throwable t = e;
        while(t.getCause() != null) {
            t = t.getCause();
        }

        if(t instanceof ConstraintViolationException) {
            return new ResponseEntity<>(new ValidationError((ConstraintViolationException) t), HttpStatus.BAD_REQUEST);
        }

        return handleGeneralApiExceptions(e, response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception e) {
        LOGGER.info("Access Denied:" + e.getMessage());
        return handleException(HttpStatus.FORBIDDEN, "Access Denied", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleGeneralApiExceptions(Exception e, HttpServletResponse response) {
        if(!(e instanceof ClientAbortException)) {
            LOGGER.error("Unexpected exception", e);
        }
        if(response.isCommitted()) {
            return null;
        }
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
    }
}
