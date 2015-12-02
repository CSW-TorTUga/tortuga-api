package st.ilu.rms4csw.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.device.Device;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
}
