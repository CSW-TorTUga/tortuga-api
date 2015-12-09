package st.ilu.rms4csw.model.reservation;

import st.ilu.rms4csw.model.device.Device;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity
public class DeviceReservation extends Reservation<DeviceReservation> {

    @OneToOne
    private Device device;

    private Optional<Device> getDevice() {
        return Optional.ofNullable(device);
    }

    public void setDevice(Optional<Device> device) {
        this.device = device.orElse(null);
    }

    @Override
    public boolean intersects(DeviceReservation other) {
        if(Objects.equals(this.device, other.device)) {
            return this.getTimeSpan().intersects(other.getTimeSpan());
        }
        return false;
    }
}
