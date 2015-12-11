package st.ilu.rms4csw.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.controller.base.CrudController;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + RoomReservationController.API_BASE)
public class RoomReservationController extends CrudController<RoomReservation> {

    public static final String API_BASE = "roomreservations";

    @Override
    @RequestMapping
    public List<RoomReservation> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping("/{id}")
    public RoomReservation findOne(String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RoomReservation> post(RoomReservation newEntity, HttpServletResponse response) {
        newEntity.setApproved(false);
        newEntity.setOpen(false);

        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public RoomReservation put(String id, RoomReservation entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public RoomReservation patch(String id, RoomReservation entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }

    @Override
    protected Class<RoomReservation> getEntityClass() {
        return RoomReservation.class;
    }

    @Autowired
    public void setReservationRepository(RoomReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }
}
