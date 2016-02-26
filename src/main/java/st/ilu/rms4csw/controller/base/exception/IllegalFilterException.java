package st.ilu.rms4csw.controller.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Mischa Holz
 */
public class IllegalFilterException extends RestException {

    private String type;

    private String field;

    public IllegalFilterException(String type, String field) {
        super(HttpStatus.BAD_REQUEST, type + " does not have a field named " + field);

        this.type = type;
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public String getField() {
        return field;
    }
}
