package st.ilu.rms4csw.repository.reservation;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Repository
public interface RoomReservationRepository extends JpaSpecificationRepository<RoomReservation, String> {

    List<RoomReservation> findByApprovedAndOpen(@Param("approved") Boolean approved, @Param("open") Boolean open);

}
