package st.ilu.rms4csw.controller.api.support;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.support.SupportMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + SupportMessageController.API_BASE)
public class SupportMessageController extends AbstractCRUDCtrl<SupportMessage> {
    public static final String API_BASE = "supportmessages";

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public List<SupportMessage> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public SupportMessage findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SupportMessage> post(@RequestBody SupportMessage newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public SupportMessage patch(@PathVariable("id") String id, @RequestBody SupportMessage entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
