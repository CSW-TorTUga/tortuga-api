package st.ilu.rms4csw.controller.api.terminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.model.terminal.OpenDoorRequest;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.reservation.RoomReservationRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;
import st.ilu.rms4csw.service.PasscodeService;
import st.ilu.rms4csw.service.TimedTokenService;
import st.ilu.rms4csw.service.door.DoorOpener;
import st.ilu.rms4csw.util.NetworkUtil;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/door", method = RequestMethod.PATCH)
    public ResponseEntity<Void> openDoorWithOpenRoomReservation(
            @RequestParam(value = "token", required = false) Long token,
            @RequestParam(value = "passcode", required = false) String passcode,
            @RequestBody OpenDoorRequest openDoorRequest) {
        if(!openDoorRequest.getOpen()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.info("Received door opening request...");

        if(passcode != null) {
            Optional<User> user = passcodeService.getUserFromPasscode(passcode);
            if(!user.isPresent()) {
                logger.info("Wrong passcode entered");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            logger.info("DOOR: Opening with passcode");
            doorOpener.openRoomDoor();

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if(token != null) {
            logger.info("Using token to authenticate");

            Optional<User> user = loggedInUserHolder.getLoggedInUser();
            if(!user.isPresent()) {
                logger.info("User is not logged in");

                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            if(timedTokenService.isValidToken(token)) {
                logger.info("DOOR: Opening with token");
                doorOpener.openRoomDoorWithoutCheckingNetwork();

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Token is invalid");

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(roomReservationRepository.findByApprovedAndOpen(true, true).stream().filter(
                r -> r.getOpenedTimeSpan().isCurrent())
                .findAny()
                .isPresent()) {

            logger.info("DOOR: Opening with open room reservation");
            doorOpener.openRoomDoor();

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ResponseEntity<Long> getCurrentDoorOpenCode(HttpServletRequest request) {
        if(!NetworkUtil.isLocalNetworkRequest(request)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

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
    public void setLoggedInUserHolder(@SuppressWarnings("SpringJavaAutowiringInspection") LoggedInUserHolder loggedInUserHolder) {
        this.loggedInUserHolder = loggedInUserHolder;
    }
}
