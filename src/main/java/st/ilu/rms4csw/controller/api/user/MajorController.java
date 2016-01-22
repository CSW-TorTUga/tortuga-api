package st.ilu.rms4csw.controller.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.repository.user.MajorRepository;

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
    public List<Major> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Major findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Major> post(@RequestBody Major newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Major put(@PathVariable("id") String id, @RequestBody Major entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Major patch(@PathVariable("id") String id, @RequestBody Major entity) {
        return super.patch(id, entity);
    }

    @Autowired
    public void setMajorRepository(MajorRepository majorRepository) {
        this.repository = majorRepository;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
