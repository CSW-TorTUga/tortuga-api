package st.ilu.rms4csw.controller.api.terminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.model.terminal.OpenDoorRequest;
import st.ilu.rms4csw.model.terminal.PasscodeAuthenticationRequest;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;
import st.ilu.rms4csw.service.PasscodeService;
import st.ilu.rms4csw.service.TimedTokenService;
import st.ilu.rms4csw.service.door.DoorOpener;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/terminal")
public class TerminalController {

    private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);

    private PasscodeService passcodeService;

    private DoorOpener doorOpener;

    private RoomReservationRepository roomReservationRepository;

    private TimedTokenService timedTokenService;

    private LoggedInUserHolder loggedInUserHolder;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Void> authenticate(@RequestBody PasscodeAuthenticationRequest passcodeAuthenticationRequest) {
        Optional<User> user = passcodeService.getUserFromPasscode(passcodeAuthenticationRequest.getPasscode());
        if(!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        doorOpener.openRoomDoor();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/door", method = RequestMethod.PATCH)
    public ResponseEntity<Void> openDoorWithOpenRoomReservation(@RequestBody OpenDoorRequest openDoorRequest) {
        User user = loggedInUserHolder.getLoggedInUser();
        logger.info("{}", user);

        if(openDoorRequest.getOpen()) {
            if(roomReservationRepository.findByApprovedAndOpen(true, true).stream().filter(
                    r -> r.getOpenedTimeSpan().isCurrent())
                    .findAny()
                    .isPresent()) {
                doorOpener.openRoomDoor();
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ResponseEntity<Long> getCurrentDoorOpenCode() {
        long currentCode = timedTokenService.getCurrentToken();

        return new ResponseEntity<>(currentCode, HttpStatus.OK);
    }

    @Autowired
    public void setPasscodeService(PasscodeService passcodeService) {
        this.passcodeService = passcodeService;
    }

    @Autowired
    public void setDoorOpener(DoorOpener doorOpener) {
        this.doorOpener = doorOpener;
    }

    @Autowired
    public void setRoomReservationRepository(RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    @Autowired
    public void setTimedTokenService(TimedTokenService timedTokenService) {
        this.timedTokenService = timedTokenService;
    }

    @Autowired
    public void setLoggedInUserHolder(LoggedInUserHolder loggedInUserHolder) {
        this.loggedInUserHolder = loggedInUserHolder;
    }
}
