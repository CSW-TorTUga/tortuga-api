package st.ilu.rms4csw.model.reservation;

import javax.persistence.Entity;

/**
 * @author Mischa Holz
 */
@Entity
@RoomReservationDoesNotIntersect
public class RoomReservation extends Reservation<RoomReservation> {

    private static final long OPEN_EXPAND_MILLIS = 15 * 60 * 1000;

    private Boolean approved;

    private String description;

    private Boolean open;

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
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

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean intersects(RoomReservation other) {
        return this.getTimeSpan().intersects(other.getTimeSpan());
    }
}
