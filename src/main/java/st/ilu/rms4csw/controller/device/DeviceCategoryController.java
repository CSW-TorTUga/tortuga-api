package st.ilu.rms4csw.controller.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.CrudController;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;
import st.ilu.rms4csw.repository.device.DeviceCategoryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceCategoryController.API_BASE)
public class DeviceCategoryController extends CrudController<DeviceCategory> {

    public static final String API_BASE = "devicecategories";


    @Override
    protected Class<DeviceCategory> getEntityClass() {
        return DeviceCategory.class;
    }

    @Override
    @RequestMapping
    public List<DeviceCategory> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping("/{id}")
    public DeviceCategory findOne(@PathVariable String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DeviceCategory> post(@RequestBody DeviceCategory newEntity, HttpServletResponse response) {
        return super.post(newEntity, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DeviceCategory put(@PathVariable String id, @RequestBody DeviceCategory entity) {
        return super.put(id, entity);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        return super.delete(id);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public DeviceCategory patch(@PathVariable String id, @RequestBody DeviceCategory entity) {
        return super.patch(id, entity);
    }

    @Autowired
    public void setDeviceCategoryRepository(DeviceCategoryRepository deviceCategoryRepository) {
        this.repository = deviceCategoryRepository;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
