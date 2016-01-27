package st.ilu.rms4csw.controller.api.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.repository.reservation.DeviceReservationRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;
import st.ilu.rms4csw.service.door.DoorOpener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceReservationController.API_BASE)
public class DeviceReservationController extends AbstractCRUDCtrl<DeviceReservation> {

    public static final String API_BASE = "devicereservations";

    private LoggedInUserHolder loggedInUserHolder;

    private DoorOpener doorOpener;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<DeviceReservation> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DeviceReservation findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DeviceReservation> post(@RequestBody DeviceReservation newEntity, HttpServletResponse response) {
        newEntity.setUser(loggedInUserHolder.getLoggedInUser());

        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DeviceReservation put(@PathVariable("id") String id, @RequestBody DeviceReservation entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public DeviceReservation patch(@PathVariable("id") String id, @RequestBody DeviceReservation entity) {
        DeviceReservation reservation = super.patch(id, entity);
        if(entity.isBorrowed() != null && (entity.isBorrowed() && reservation.isBorrowed())) {
            doorOpener.openCabinetDoor(reservation.getDevice().getCabinet());
        }

        return reservation;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }

    @Autowired
    public void setReservationRepository(DeviceReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    @Autowired
    public void setLoggedInUserHolder(LoggedInUserHolder loggedInUserHolder) {
        this.loggedInUserHolder = loggedInUserHolder;
    }

    @Autowired
    public void setDoorOpener(DoorOpener doorOpener) {
        this.doorOpener = doorOpener;
    }
}
