package st.ilu.rms4csw.controller.api.device;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.controller.base.ChangeSet;
import st.ilu.rms4csw.controller.base.response.BadRequestResponse;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceCategoryController.API_BASE)
public class DeviceCategoryController extends AbstractCRUDCtrl<DeviceCategory> {

    public static final String API_BASE = "devicecategories";

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<DeviceCategory> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DeviceCategory findOne(@PathVariable String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity<DeviceCategory> post(@RequestBody DeviceCategory newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            return super.delete(id);
        } catch(DataIntegrityViolationException e) {
            throw new BadRequestResponse("Kategorien mit Geräten können nicht gelöscht werden. Bitte lösche zuerst alle Geräte.");
        }
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasAuthority('OP_TEAM')")
    public DeviceCategory patch(@PathVariable String id, @RequestBody ChangeSet<DeviceCategory> entity) {
        return super.patch(id, entity);
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
