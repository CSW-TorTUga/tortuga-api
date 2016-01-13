package st.ilu.rms4csw;

import st.ilu.rms4csw.model.cabinet.Cabinet;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;
import st.ilu.rms4csw.model.user.Gender;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.model.user.User;

import java.util.Optional;

/**
 * @author Mischa Holz
 */
public class TestHelper {

    public static User createUser() {
        User user = new User();
        user.setExpirationDate(Optional.empty());
        user.setPhoneNumber("123456789");
        user.setRole(Role.ADMIN);
        user.setFirstName("Admin");
        user.setLastName("Admington");
        user.setGender(Optional.of(Gender.FEMALE));
        user.setStudentId(Optional.empty());
        user.setMajor(Optional.empty());
        user.setEmail("admin@ilu.st");
        user.setLoginName("admin");
        user.setPassword("change me.");
        return user;
    }

    public static Device createDevice(DeviceCategory deviceCategory) {
        Device device = new Device();
        device.setInventoryNumber("inv number");
        device.setDescription("beschreibung");
        device.setCategory(deviceCategory);
        device.setName("name");
        device.setAccessories("");
        device.setAcquisitionDate(Optional.empty());
        device.setCabinet(Cabinet.CABINET_6);

        return device;
    }

    public static Device createOtherDevice(DeviceCategory deviceCategory) {
        Device device = new Device();
        device.setInventoryNumber("other number");
        device.setDescription("andere beschreibung");
        device.setCategory(deviceCategory);
        device.setName("anderer name");
        device.setAccessories("");
        device.setAcquisitionDate(Optional.empty());
        device.setCabinet(Cabinet.CABINET_7);

        return device;
    }

    public static DeviceCategory createDeviceCategory() {
        DeviceCategory deviceCategory = new DeviceCategory();
        deviceCategory.setActive(true);
        deviceCategory.setName("Kategorie");

        return deviceCategory;
    }

}
