package st.ilu.rms4csw.model.reservation;

import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.model.user.PossessedEntity;
import st.ilu.rms4csw.model.user.User;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Mischa Holz
 */
@MappedSuperclass
public abstract class Reservation<T extends Reservation> extends PersistentEntity implements PossessedEntity {

    @OneToOne
    @NotNull(message = "Reeservierungen müssen einem Benutzer zugewiesen sein")
    private User user;

    @Embedded
    @NotNull(message = "Reservierungen brauchen einen Zeitraum")
    @TimeSpanIsValid
    private TimeSpan timeSpan;

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

    public abstract boolean intersects(T other);
}
