package st.ilu.rms4csw.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceCategoryRepository extends JpaRepository<DeviceCategory, String> {
}
