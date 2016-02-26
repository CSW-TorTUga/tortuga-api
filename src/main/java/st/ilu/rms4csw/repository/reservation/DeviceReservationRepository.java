package st.ilu.rms4csw.repository.reservation;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Repository
public interface DeviceReservationRepository extends JpaSpecificationRepository<DeviceReservation, String> {

    List<DeviceReservation> findAllByDeviceId(@Param("id") String id);

    List<DeviceReservation> findAllByUserId(@Param("id") String id, Sort sort);

    List<DeviceReservation> findAllByUserIdAndDeviceCategoryId(@Param("userId") String userId, @Param("deviceCategoryId") String deviceCategoryId, Sort sort);

    List<DeviceReservation> findAllByDeviceIdAndBorrowed(@Param("deviceId") String deviceId, @Param("borrowed") Boolean borrowed);
}
