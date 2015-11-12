package st.ilu.rms4csw.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Mischa Holz
 */
public class ValidationError extends ErrorResponse {

    private Map<String, String> errors = new LinkedHashMap<>();

    public ValidationError(ConstraintViolationException e) {
        super(400, "Could not validate object");

        for(ConstraintViolation cv : e.getConstraintViolations()) {
            String key = cv.getPropertyPath().toString();
            String value = cv.getMessage();

            errors.put(key, value);
        }
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
}
