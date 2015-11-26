package st.ilu.rms4csw.patch;

import java.lang.reflect.Field;

/**
 * @author Mischa Holz
 */
public class Patch {

    public static <T> T patch(T base, T patch) {
        if(!base.getClass().equals(patch.getClass())) {
            throw new IllegalArgumentException("Both objects need to be the exact same class");
        }

        Field[] fields = base.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value;
                value = field.get(patch);
                if(value != null) {
                    field.set(base, value);
                }
            } catch (IllegalAccessException e) {
                // impossible
            }
        }

        return base;
    }

}
