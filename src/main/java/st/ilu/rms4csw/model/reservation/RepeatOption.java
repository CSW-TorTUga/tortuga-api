package st.ilu.rms4csw.model.reservation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Mischa Holz
 */
public enum RepeatOption {

    WEEKLY("wöchentlich", 1),
    BIWEEKLY("alle zwei Wochen", 2),
    TRIWEEKLY("alle drei Wochen", 3),
    QUADWEEKLY("alle vier Wochen", 4);

    private final int weekDiff;
    private String displayName;

    RepeatOption(String displayName, int weekDiff) {
        this.displayName = displayName;
        this.weekDiff = weekDiff;
    }

    public List<Date> calculateDates(Date startDate, Date endRepeatDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        List<Date> ret = new ArrayList<>();
        ret.add(startDate);

        while(true) {
            calendar.set(Calendar.WEEK_OF_YEAR, currentWeek + weekDiff);

            Date date = calendar.getTime();
            if(date.after(endRepeatDate)) {
                break;
            }

            ret.add(date);

            currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        }

        return ret;
    }

    @JsonValue
    public String displayName() {
        return displayName;
    }

    @JsonCreator
    public static RepeatOption fromValue(String val) {
        for (RepeatOption repeatOption : RepeatOption.values()) {
            if(repeatOption.displayName().equals(val)) {
                return repeatOption;
            }
        }

        throw new IllegalArgumentException("Could not map '" + val + "' to any RepeatOption");
    }
}
