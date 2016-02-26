package st.ilu.rms4csw.controller.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.controller.base.exception.NotFoundException;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.security.LoggedInUserHolder;
import st.ilu.rms4csw.service.PasscodeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + UserController.USER_API_BASE)
public class UserController extends AbstractCRUDCtrl<User> {

    public final static String USER_API_BASE = "users";

    @Override
    public String getApiBase() {
        return USER_API_BASE;
    }

    private LoggedInUserHolder loggedInUserHolder;

    private PasscodeService passcodeService;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PostFilter("filterObject.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public List<User> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PostAuthorize("returnObject.id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public User findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @RequestMapping(value = "/{id}/passcode", method = RequestMethod.POST)
    @PreAuthorize("#id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public Object generatePasscode(@PathVariable("id") String id) throws InterruptedException {
        Thread.sleep(500);

        User user = repository.findOne(id);
        if(user == null) {
            throw new NotFoundException("Didn't find user");
        }

        List<String> passcodeList = passcodeService.generateRandomPasscode();
        String passcodeStr = passcodeList.stream().reduce("", (a, b) -> a + b);
        user.setPasscode(Optional.of(passcodeStr));

        repository.save(user);

        return new Object() {
            public List<String> passcode = passcodeList;

            public List<String> getPasscode() {
                return passcode;
            }
        };
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("(hasAuthority('OP_TEAM') && #user.getRole() != T(st.ilu.rms4csw.model.user.Role).ADMIN) || hasAuthority('OP_ADMIN')")
    public ResponseEntity<User> post(@RequestBody User user, HttpServletResponse response) {
        if(user.getRole() == Role.STUDENT) {
            Date expires = User.calculateNextSemesterEnd(new Date());
            user.setExpirationDate(Optional.of(expires));
        }

        if(user.getRole() == Role.ADMIN) {
            user.setExpirationDate(Optional.empty());
        }

        return super.post(user, response);
    }

    @Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("@userService.canUserDelete(authentication.getDetails(), #id)")
	public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
	}

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("#id.equals(authentication.getPrincipal()) || hasAuthority('OP_TEAM')")
    public User patch(@PathVariable("id") String id, @RequestBody ChangeSet<User> user) {
        User beforeUpdate = repository.findOne(id);
        if(beforeUpdate == null) {
            throw new NotFoundException("Can't find user");
        }

        if(beforeUpdate.getRole() == Role.STUDENT && (user.getPatch().getRole() == null || user.getPatch().getRole() == Role.STUDENT)) {
            if(!user.getPatch().getExpirationDate().isPresent()) {
                Date expires = beforeUpdate.getExpirationDate().orElse(User.calculateNextSemesterEnd(new Date()));
                user.getPatch().setExpirationDate(Optional.of(expires));
            }
        }

        if(user.getPatch().getRole() == Role.STUDENT && (!beforeUpdate.getExpirationDate().isPresent())) {
            Date expires = User.calculateNextSemesterEnd(new Date());
            user.getPatch().setExpirationDate(Optional.of(expires));
        }

        if(beforeUpdate.getRole() != Role.STUDENT && user.getPatch().getRole() != Role.STUDENT) {
            user.getPatch().setExpirationDate(Optional.empty());
        }

        return super.patch(id, user);
    }

    @Autowired
    public void setLoggedInUserHolder(LoggedInUserHolder loggedInUserHolder) {
        this.loggedInUserHolder = loggedInUserHolder;
    }

    @Autowired
    public void setPasscodeService(PasscodeService passcodeService) {
        this.passcodeService = passcodeService;
    }
}
