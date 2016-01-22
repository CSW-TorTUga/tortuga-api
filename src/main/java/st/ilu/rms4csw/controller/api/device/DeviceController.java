package st.ilu.rms4csw.controller.api.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.repository.device.DeviceRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceController.API_BASE)
public class DeviceController extends AbstractCRUDCtrl<Device> {

    public static final String API_BASE = "devices";

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<Device> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Device findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Device> post(@RequestBody Device newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Device put(@PathVariable("id") String id, @RequestBody Device entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Device patch(@PathVariable("id") String id, @RequestBody Device entity) {
        return super.patch(id, entity);
    }

    @Autowired
    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.repository = deviceRepository;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
