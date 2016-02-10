package st.ilu.rms4csw.controller.api.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.exception.NotFoundException;
import st.ilu.rms4csw.model.cabinet.Cabinet;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.model.reservation.TimeSpan;
import st.ilu.rms4csw.security.LoggedInUserHolder;
import st.ilu.rms4csw.service.door.DoorOpener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
    @PostFilter("filterObject.user.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public List<DeviceReservation> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PostAuthorize("returnObject.user.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public DeviceReservation findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("#newEntity.user.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public ResponseEntity<DeviceReservation> post(@RequestBody DeviceReservation newEntity, HttpServletResponse response) {
        newEntity.setUser(loggedInUserHolder.getLoggedInUser());

        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("#entity.user.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public DeviceReservation put(@PathVariable("id") String id, @RequestBody DeviceReservation entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("@possessedEntityPermissionElevator.checkOwner(@deviceReservationRepository, #id, authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("@possessedEntityPermissionElevator.checkOwner(@deviceReservationRepository, #id, authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public DeviceReservation patch(@PathVariable("id") String id, @RequestBody DeviceReservation entity) {
        DeviceReservation old = repository.findOne(id);
        if(old == null) {
            throw new NotFoundException("Could not find DeviceReservation with id " + id);
        }

        Boolean oldBorrowed = old.isBorrowed() == null ? false : old.isBorrowed();

        DeviceReservation reservation = super.patch(id, entity);

        Boolean newBorrowed = reservation.isBorrowed() == null ? false : reservation.isBorrowed();

        if(oldBorrowed != newBorrowed) {
            doorOpener.openCabinetDoor(reservation.getDevice().getCabinet());
            if(newBorrowed) {
                reservation.setBorrowedBeginning(new Date());
            } else {
                TimeSpan timeSpan = new TimeSpan();
                timeSpan.setBeginning(old.getBorrowedBeginning());
                timeSpan.setEnd(new Date());

                reservation.setBorrowedBeginning(null);
                reservation.getBorrowedTimeSpans().add(timeSpan);
            }

            repository.save(reservation);
        }

        return reservation;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
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
