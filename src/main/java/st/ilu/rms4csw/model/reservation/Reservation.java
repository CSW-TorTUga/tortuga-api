package st.ilu.rms4csw.model.reservation;

import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.model.device.Device;
import st.ilu.rms4csw.model.user.User;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity
public class Reservation extends PersistentEntity {

    @OneToOne
    @NotNull
    private User user;

    @Embedded
    @NotNull
    private TimeSpan timeSpan;

    @OneToOne
    private Device reservedDevice;

    private boolean reserveRoom;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    private Optional<Device> getReservedDevice() {
        return Optional.ofNullable(reservedDevice);
    }

    public void setReservedDevice(Optional<Device> reservedDevice) {
        this.reservedDevice = reservedDevice.orElse(null);
    }

    public boolean isReserveRoom() {
        return reserveRoom;
    }

    public void setReserveRoom(boolean reserveRoom) {
        this.reserveRoom = reserveRoom;
    }

    public boolean intersects(Reservation other) {
        if(this.reserveRoom == other.reserveRoom && Objects.equals(this.reservedDevice, other.reservedDevice)) {
            return this.timeSpan.intersects(other.timeSpan);
        }

        return false;
    }
}
