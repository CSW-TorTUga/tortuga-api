package st.ilu.rms4csw.controller.base;

import st.ilu.rms4csw.model.base.PersistentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mischa Holz
 */
public class ChangeSet<T extends PersistentEntity> {

    private T patch;

    private List<String> patchedFields = new ArrayList<>();

    public T getPatch() {
        return patch;
    }

    public void setPatch(T patch) {
        this.patch = patch;
    }

    public List<String> getPatchedFields() {
        return patchedFields;
    }

    public void setPatchedFields(List<String> patchedFields) {
        this.patchedFields = patchedFields;
    }
}
