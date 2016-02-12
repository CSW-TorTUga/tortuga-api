package st.ilu.rms4csw.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * @author Mischa Holz
 */
@Service
public class TimedTokenService {

    private final SecureRandom secureRandom = new SecureRandom();

    public long getCurrentToken() {
        return calculateToken(System.currentTimeMillis());
    }

    public long getPreviousToken() {
        return calculateToken(System.currentTimeMillis() - (30 * 1000));
    }

    private long calculateToken(long time) {
        long currentInterval = time / (30 * 1000);

        synchronized(secureRandom) {
            secureRandom.setSeed(currentInterval);
            secureRandom.set

            return secureRandom.nextInt(999_999 - 100_000) + 100_000;
        }
    }

    public boolean isValidToken(long token) {
        return token == getCurrentToken() || token == getPreviousToken();
    }

}
