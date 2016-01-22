package st.ilu.rms4csw.model.devicecategory;

import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
public class DeviceCategory extends PersistentEntity {

    @NotEmpty(message = "Der Kategorienname darf nicht leer sein")
    private String name;

    private Boolean active = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
