package st.ilu.rms4csw.repository.reservation;

import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * @author Mischa Holz
 */
@Repository
public interface RoomReservationRepository extends JpaSpecificationRepository<RoomReservation, String> {
}
