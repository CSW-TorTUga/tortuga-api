package st.ilu.rms4csw.model.device;

import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;

/**
 * @author Mischa Holz
 */
public class Device extends PersistentEntity {

    private String name;

    private DeviceCategory deviceCategory;

}
