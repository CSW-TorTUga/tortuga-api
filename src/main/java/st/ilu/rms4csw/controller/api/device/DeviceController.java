package st.ilu.rms4csw.controller.api.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.reservation.DeviceReservation;
import st.ilu.rms4csw.model.reservation.TimeSpan;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.device.DeviceRepository;
import st.ilu.rms4csw.repository.reservation.DeviceReservationRepository;
import st.ilu.rms4csw.security.LoggedInUserHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + DeviceController.API_BASE)
public class DeviceController extends AbstractCRUDCtrl<Device> {

    public static final String API_BASE = "devices";

    private DeviceReservationRepository deviceReservationRepository;

    private LoggedInUserHolder loggedInUserHolder;

    private DeviceRepository deviceRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Device> findAll(HttpServletRequest request,
                                @RequestParam(value = "beginningTime", required = false) Long beginningTime,
                                @RequestParam(value = "endTime", required = false) Long endTime,
                                @RequestParam(value = "category", required = false) String categoryId) {
        if(beginningTime == null && endTime == null) {
            return super.findAll(request);
        } else if(beginningTime != null && endTime != null && categoryId != null) {
            return suggestDevice(beginningTime, endTime, categoryId);
        } else {
            throw new IllegalArgumentException("Um Geräte vorgeschlagen zu bekommen müssen beginningTime, endTime und category angegeben sein");
        }
    }

    private List<Device> suggestDevice(Long beginningTime, Long endTime, String categoryId) {
        TimeSpan timeSpan = new TimeSpan(new Date(beginningTime), new Date(endTime));

        User user = loggedInUserHolder.getLoggedInUser();

        List<DeviceReservation> reservations = deviceReservationRepository.findAllByUserIdAndDeviceCategoryId(user.getId(), categoryId, new Sort(Sort.Direction.DESC, "timeSpan.end"));

        List<Device> devicesFromCategory = deviceRepository.findByCategoryId(categoryId);
        if(reservations.size() == 0) {
            return filterAvailableDevices(timeSpan, devicesFromCategory);
        }

        List<Device> suggestions = Stream.concat(reservations.stream().map(DeviceReservation::getDevice), devicesFromCategory.stream())
                .distinct()
                .collect(Collectors.toList());

        return filterAvailableDevices(timeSpan, suggestions);
    }

    private List<Device> filterAvailableDevices(TimeSpan timeSpan, List<Device> devices) {
        List<Device> ret = new ArrayList<>();

        for(Device device : devices) {
            List<DeviceReservation> reservations = deviceReservationRepository.findAllByDeviceId(device.getId());

            boolean conflicts = false;

            for(DeviceReservation reservation : reservations) {
                if(reservation.getTimeSpan().intersects(timeSpan)) {
                    conflicts = true;
                }
            }

            if(!conflicts) {
                ret.add(device);
            }
        }

        return ret;
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
        this.deviceRepository = deviceRepository;
    }

    @Autowired
    public void setDeviceReservationRepository(DeviceReservationRepository deviceReservationRepository) {
        this.deviceReservationRepository = deviceReservationRepository;
    }

    @Autowired
    public void setLoggedInUserHolder(LoggedInUserHolder loggedInUserHolder) {
        this.loggedInUserHolder = loggedInUserHolder;
    }

    @Override
    public String getApiBase() {
        return API_BASE;
    }
}
