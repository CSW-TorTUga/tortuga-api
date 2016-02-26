package st.ilu.rms4csw.controller.api.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.model.support.SupportMessage;
import st.ilu.rms4csw.service.EmailService;

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

    @Autowired
    private EmailService emailService;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity<List<SupportMessage>> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity<SupportMessage> findOne(@PathVariable("id") String id) {
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
    public ResponseEntity<SupportMessage> patch(@PathVariable("id") String id, @RequestBody ChangeSet<SupportMessage> entity) {
        if(entity.getPatch().getAnswer().isPresent()) {
            SupportMessage supportMessage = repository.findOne(id);

            emailService.sendEmail(
                    supportMessage.getEmail().orElseThrow(() -> new IllegalArgumentException("That SupoprtMessage does not have an email associated with it")),
                    supportMessage.getSubject(),
                    entity.getPatch().getAnswer().get());
        }

        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
