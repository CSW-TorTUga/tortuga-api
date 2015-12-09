package st.ilu.rms4csw.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.controller.base.CrudController;
import st.ilu.rms4csw.model.reservation.Reservation;
import st.ilu.rms4csw.repository.reservation.ReservationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + ReservationController.API_BASE)
public class ReservationController extends CrudController<Reservation> {

    public static final String API_BASE = "reservations";

    @Override
    @RequestMapping
    public List<Reservation> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping("/{id}")
    public Reservation findOne(String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Reservation> post(Reservation newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Reservation put(String id, Reservation entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Reservation patch(String id, Reservation entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }

    @Override
    protected Class<Reservation> getEntityClass() {
        return Reservation.class;
    }

    @Autowired
    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }
}
