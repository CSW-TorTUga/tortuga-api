package st.ilu.rms4csw.controller.api.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.controller.base.response.BadRequestResponse;
import st.ilu.rms4csw.model.major.Major;

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
        try {
            return super.delete(id);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestResponse("Es gibt noch Benutzer mit diesem Studienfach. Es kann nicht gelöscht werden.");
        }
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
}
