package st.ilu.rms4csw.service;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Mischa Holz
 */
@Service
public class TimedTokenService {

    public long getCurrentToken() {
        return calculateToken(System.currentTimeMillis());
    }

    public long getPreviousToken() {
        return calculateToken(System.currentTimeMillis() - (30 * 1000));
    }

    private long calculateToken(long time) {
        long currentInterval = time / (30 * 1000);

        Random random = new Random();
        random.setSeed(currentInterval);

        return random.nextInt(999_999 - 100_000) + 100_000;
    }

    public boolean isValidToken(long token) {
        return token == getCurrentToken() || token == getPreviousToken();
    }

}
