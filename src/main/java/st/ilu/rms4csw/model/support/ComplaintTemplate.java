package st.ilu.rms4csw.model.support;

import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
public class ComplaintTemplate extends PersistentEntity {

    @NotEmpty(message = "Beschwerevorlagen brauchen einen Text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
