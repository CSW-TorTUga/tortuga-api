package st.ilu.rms4csw;

import org.junit.Test;
import st.ilu.rms4csw.model.reservation.TimeSpan;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Mischa Holz
 */
public class TimeSpanTest {

    @Test
    public void testIntersectionOfSameTimespans() {
        TimeSpan one = new TimeSpan(new Date(100), new Date(200));

        assertTrue("The same timespan needs to intersect with itself", one.intersects(one));
    }

    @Test
    public void testIntersectionOfTimespans() {
        TimeSpan one = new TimeSpan(new Date(100), new Date(200));
        TimeSpan two = new TimeSpan(new Date(150), new Date(250));

        assertTrue("These two timespans need to intersect", one.intersects(two));

        assertTrue("These two timespans need to intersect", two.intersects(one));
    }

    @Test
    public void testOtherIntersectionOfTimespans() {
        TimeSpan one = new TimeSpan(new Date(100), new Date(200));
        TimeSpan two = new TimeSpan(new Date(50), new Date(150));

        assertTrue("These two timespans need to intersect", one.intersects(two));

        assertTrue("These two timespans need to intersect", two.intersects(one));
    }

    @Test
    public void testSuperPositionOfTimeSpans() {
        TimeSpan one = new TimeSpan(new Date(100), new Date(200));
        TimeSpan two = new TimeSpan(new Date(50), new Date(250));

        assertTrue("These two timespans need to intersect", one.intersects(two));

        assertTrue("These two timespans need to intersect", two.intersects(one));
    }

    @Test
    public void testTimeSpansNotIntersecting() {
        TimeSpan one = new TimeSpan(new Date(100), new Date(200));
        TimeSpan two = new TimeSpan(new Date(201), new Date(250));

        assertFalse("These two timespans should not intersect", one.intersects(two));

        assertFalse("These two timespans should not intersect", two.intersects(one));
    }

}
