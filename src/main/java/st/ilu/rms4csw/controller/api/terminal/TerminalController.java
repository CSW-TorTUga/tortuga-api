package st.ilu.rms4csw.controller.api.terminal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.model.terminal.PasscodeAuthenticationRequest;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.service.PasscodeService;
import st.ilu.rms4csw.service.door.DoorOpener;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/terminal")
public class TerminalController {

    private PasscodeService passcodeService;

    private DoorOpener doorOpener;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody PasscodeAuthenticationRequest passcodeAuthenticationRequest) {
        Optional<User> user = passcodeService.getUserFromPasscode(passcodeAuthenticationRequest.getPasscode());
        if(!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        doorOpener.openRoomDoor();

        return new ResponseEntity<>(new Object() {
            private boolean success = true;

            public boolean isSuccess() {
                return success;
            }
        }, HttpStatus.OK);
    }

    @Autowired
    public void setPasscodeService(PasscodeService passcodeService) {
        this.passcodeService = passcodeService;
    }

    @Autowired
    public void setDoorOpener(DoorOpener doorOpener) {
        this.doorOpener = doorOpener;
    }
}
