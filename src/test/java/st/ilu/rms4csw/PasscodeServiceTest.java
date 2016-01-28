package st.ilu.rms4csw;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import st.ilu.rms4csw.service.PasscodeService;

import static org.junit.Assert.assertTrue;

/**
 * @author Mischa Holz
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestContext.class)
@WebAppConfiguration
public class PasscodeServiceTest {

    @Autowired
    private PasscodeService passcodeService;

    @Test
    public void testSplitEmojis() throws Exception {
        assertTrue("There have to be 42 emojis", passcodeService.getPossibleCharacters().size() == 42);
    }
}