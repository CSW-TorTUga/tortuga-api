package st.ilu.rms4csw;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import st.ilu.rms4csw.service.SpringSecurityLoggedInUserHolder;

/**
 * @author Mischa Holz
 */
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SpringSecurityLoggedInUserHolder.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = Main.class)
})
public class TestContext {
}
