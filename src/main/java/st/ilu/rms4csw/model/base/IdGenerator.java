package st.ilu.rms4csw.model.base;

import java.util.UUID;

/**
 * @author Mischa Holz
 */
public class IdGenerator {

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
