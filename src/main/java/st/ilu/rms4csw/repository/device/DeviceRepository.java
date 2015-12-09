package st.ilu.rms4csw.repository.device;

import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceRepository extends JpaSpecificationRepository<Device, String> {
}
