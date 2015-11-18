package st.ilu.rms4csw.model.major;

import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
public class Major extends PersistentEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
