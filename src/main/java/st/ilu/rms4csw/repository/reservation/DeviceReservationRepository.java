package st.ilu.rms4csw.repository.reservation;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceReservationRepository extends JpaSpecificationRepository<DeviceReservation, String> {

    List<DeviceReservation> findAllByDeviceId(String id);

    List<DeviceReservation> findAllByUserId(String id, Sort sort);

    List<DeviceReservation> findAllByUserIdAndDeviceCategoryId(String userId, String deviceCategoryId, Sort sort);
}
