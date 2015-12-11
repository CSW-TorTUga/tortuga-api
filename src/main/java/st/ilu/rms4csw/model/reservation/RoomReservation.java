package st.ilu.rms4csw.model.reservation;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
public class RoomReservation extends Reservation<RoomReservation> {

    private static final long OPEN_EXPAND_MILLIS = 15 * 60 * 1000;

    private boolean approved;

    private String description;

    private boolean open;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public TimeSpan getOpenedTimeSpan() {
        return this.getTimeSpan().expand(OPEN_EXPAND_MILLIS);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean intersects(RoomReservation other) {
        return this.getTimeSpan().intersects(other.getTimeSpan());
    }
}
