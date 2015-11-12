package st.ilu.rms4csw.model.user;

import st.ilu.rms4csw.base.PersistentEntity;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
public class Role extends PersistentEntity {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
