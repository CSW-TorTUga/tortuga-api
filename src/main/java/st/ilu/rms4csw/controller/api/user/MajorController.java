package st.ilu.rms4csw.controller.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.controller.base.response.BadRequestResponse;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + MajorController.API_BASE)
public class MajorController extends AbstractCRUDCtrl<Major> {

	public final static String API_BASE = "majors";

    private UserRepository userRepository;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public List<Major> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public Major findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity<Major> post(@RequestBody Major newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity delete(@PathVariable("id") String id) {
        List<User> users = userRepository.findAllByMajorId(id);
        if(users.size() > 0) {
            throw new BadRequestResponse("Es existieren noch Benutzer, die diesen Studiengang haben. Diese Benutzer müssen gelöscht oder bearbeitet werden bevor der Studiengang gelöscht werden kann.");
        }

        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public Major patch(@PathVariable("id") String id, @RequestBody ChangeSet<Major> entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
