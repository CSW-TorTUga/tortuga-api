package st.ilu.rms4csw.controller.base.exception;

/**
 * @author Mischa Holz
 */
public class IllegalFilterException extends IllegalArgumentException {

    private String type;

    private String field;

    public IllegalFilterException(String type, String field) {
        super(type + " does not have a field named " + field);

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
