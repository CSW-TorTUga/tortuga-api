package st.ilu.rms4csw.controller.api.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.support.ComplaintTemplate;
import st.ilu.rms4csw.repository.support.ComplaintTemplateRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + ComplaintTemplateController.API_BASE)
public class ComplaintTemplateController extends AbstractCRUDCtrl<ComplaintTemplate> {

	public final static String API_BASE = "complainttemplates";

    @Override
    @RequestMapping
    public List<ComplaintTemplate> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping("/{id}")
    public ComplaintTemplate findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ComplaintTemplate> post(@RequestBody ComplaintTemplate newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ComplaintTemplate put(@PathVariable("id") String id, @RequestBody ComplaintTemplate entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ComplaintTemplate patch(@PathVariable("id") String id, @RequestBody ComplaintTemplate entity) {
        return super.patch(id, entity);
    }

    @Autowired
    public void setComplaintTemplateRepository(ComplaintTemplateRepository majorRepository) {
        this.repository = majorRepository;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
