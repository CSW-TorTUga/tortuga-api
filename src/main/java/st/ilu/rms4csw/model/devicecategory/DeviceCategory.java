package st.ilu.rms4csw.model.devicecategory;

import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author Mischa Holz
 */
@Entity
public class DeviceCategory extends PersistentEntity {

    @NotNull
    @NotEmpty
    private String name;

    private Boolean active;

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
