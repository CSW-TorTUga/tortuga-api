package st.ilu.rms4csw.controller.api.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.reservation.RoomReservation;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;
import st.ilu.rms4csw.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + RoomReservationController.API_BASE)
public class RoomReservationController extends AbstractCRUDCtrl<RoomReservation> {

    public static final String API_BASE = "roomreservations";

    private UserService userService;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PostFilter("(filterObject.approved != null && filterObject.approved) || hasAuthority('OP_TEAM')")
    public List<RoomReservation> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PostAuthorize("(returnObject.approved != null && returnObject.approved) || hasAuthority('OP_TEAM')")
    public RoomReservation findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('OP_LECTURER')")
    public ResponseEntity<RoomReservation> post(@RequestBody RoomReservation newEntity, HttpServletResponse response) {
        User user = userService.getLoggedInUser();
        newEntity.setUser(user);

        newEntity.setApproved(false);
        newEntity.setOpen(false);

        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("@possessedEntityPermissionElevator.checkOwner(@roomReservationRepository, #id, authentication.getUser().getId()) || hasAuthority('OP_TEAM')")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public RoomReservation patch(@PathVariable("id") String id, @RequestBody RoomReservation entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }

    @Autowired
    public void setReservationRepository(RoomReservationRepository reservationRepository) {
        this.repository = reservationRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
