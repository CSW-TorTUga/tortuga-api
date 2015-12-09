package st.ilu.rms4csw.model.reservation;

/**
 * @author Mischa Holz
 */
public class RoomReservation extends Reservation<RoomReservation> {

    private boolean approved;

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean intersects(RoomReservation other) {
        return other.getTimeSpan().intersects(other.getTimeSpan());
    }
}
