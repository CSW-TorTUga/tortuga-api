package st.ilu.rms4csw.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Mischa Holz
 */
@MappedSuperclass
public abstract class PersistentEntity implements Serializable {

    private static final long serialVersionUID = -9103267971270130529L;

    @Id
    @Access(AccessType.FIELD)
    @Column(length = 32)
    @Pattern(regexp = "[0-9a-f]{32}")

    @JsonIgnore
    private String id;

    public PersistentEntity() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistentEntity)) return false;

        PersistentEntity that = (PersistentEntity) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
