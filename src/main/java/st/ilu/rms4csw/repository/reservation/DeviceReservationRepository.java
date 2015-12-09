package st.ilu.rms4csw.repository.reservation;

import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceReservationRepository extends JpaSpecificationRepository<DeviceReservation, String> {
}
