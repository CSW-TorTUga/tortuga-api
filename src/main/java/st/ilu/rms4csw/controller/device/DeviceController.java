package st.ilu.rms4csw.controller.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.CrudController;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.repository.device.DeviceRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceController.API_BASE)
public class DeviceController extends CrudController<Device> {

    public static final String API_BASE = "devices";

    @Override
    @RequestMapping
    public List<Device> findAll() {
        return super.findAll();
    }

    @Override
    @RequestMapping("/{id}")
    public Device findOne(@PathVariable String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Device> post(@RequestBody Device newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Device put(@PathVariable String id, @RequestBody Device entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Device patch(@PathVariable String id, @RequestBody Device entity) {
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
