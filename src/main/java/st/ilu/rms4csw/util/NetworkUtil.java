package st.ilu.rms4csw.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mischa Holz
 */
public class NetworkUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

    public static boolean isLocalNetworkRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if(sra == null) {
            logger.info("No request attached to this thread.");
            return false;
        }

        HttpServletRequest request = sra.getRequest();
        String realIp = request.getHeader("X-Real-IP");
        logger.info("X-Real-IP: {}", realIp);

        String forwardedFor = request.getHeader("X-Forwarded-For");
        logger.info("X-Forwarded-For: {}", forwardedFor);

        String remoteAddr = request.getRemoteAddr();
        logger.info("RemoteAddr: {}", remoteAddr);


        if("true".equals(System.getenv("ALLOW_DOOR_FROM_EVERYWHERE"))) {
            return true;
        }

        if(request.getHeader("X-Real-IP") != null) {
            return realIp.startsWith("192.168");
        }

        if(request.getHeader("X-Forwarded-For") != null) {
            return forwardedFor.startsWith("192.168");
        }

        return remoteAddr.startsWith("192.168");
    }

}
