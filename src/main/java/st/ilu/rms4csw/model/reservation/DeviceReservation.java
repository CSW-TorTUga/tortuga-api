package st.ilu.rms4csw.model.reservation;

import st.ilu.rms4csw.model.device.Device;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Mischa Holz
 */
@Entity
public class DeviceReservation extends Reservation<DeviceReservation> {

    @OneToOne
    @NotNull
    private Device device;

    private boolean borrowed;

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public boolean intersects(DeviceReservation other) {
        if(Objects.equals(this.device, other.device)) {
            return this.getTimeSpan().intersects(other.getTimeSpan());
        }
        return false;
    }
}
