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

    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
