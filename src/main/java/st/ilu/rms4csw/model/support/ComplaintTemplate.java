package st.ilu.rms4csw.model.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ComplaintTemplate extends PersistentEntity {

    @NotEmpty(message = "Beschwerdevorlagen brauchen einen Text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
