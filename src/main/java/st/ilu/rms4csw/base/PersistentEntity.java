package st.ilu.rms4csw.base;

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
    private String id;

    @Version
    private int version = 0;

    public PersistentEntity() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
