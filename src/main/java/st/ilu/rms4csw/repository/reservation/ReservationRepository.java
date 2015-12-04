package st.ilu.rms4csw.repository.reservation;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.reservation.Reservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Repository
public interface ReservationRepository extends JpaSpecificationRepository<Reservation, String> {

    List<Reservation> findByReservedDevice(Device reservedDevice);

    @Query("select r from Reservation r where r.reserveRoom = true")
    List<Reservation> findRoomReservations();
}
