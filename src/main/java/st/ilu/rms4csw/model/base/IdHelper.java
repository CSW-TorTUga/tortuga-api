package st.ilu.rms4csw.model.base;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * @author Mischa Holz
 */
public class IdHelper {

    private static Field idField = null;

    static {
        try {
            idField = PersistentEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setId(PersistentEntity obj, String id) {
        try {
            idField.set(obj, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assignNewId(PersistentEntity obj) {
        try {
            idField.set(obj, UUID.randomUUID().toString().replace("-", ""));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makeLegit(PersistentEntity obj) {
        String id = obj.getId();
        if(!id.matches("[0-9a-f]{32}")) {
            assignNewId(obj);
        }
    }
}
