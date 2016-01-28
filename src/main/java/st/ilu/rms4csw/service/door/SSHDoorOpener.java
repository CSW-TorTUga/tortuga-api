package st.ilu.rms4csw.service.door;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import st.ilu.rms4csw.model.cabinet.Cabinet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Mischa Holz
 */
public class SSHDoorOpener implements DoorOpener {

    private static Logger logger = LoggerFactory.getLogger(DoorOpener.class);

    private String host;

    private String user;

    private String password;

    public SSHDoorOpener(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    private void executeCommandOnHost(String cmdStr) {
        try(SSHClient ssh = new SSHClient()) {
            ssh.loadKnownHosts();

            ssh.connect(host);

            ssh.authPassword(user, password);

            try(Session session = ssh.startSession()) {
                Session.Command cmd = session.exec(cmdStr);
                cmd.join(5, TimeUnit.SECONDS);
            }
        } catch(IOException e) {
            throw new RuntimeException();
        }
    }

    private boolean isLocalNetworkRequest() {
        if("true".equals(System.getenv("ALLOW_DOOR_FROM_EVERYWHERE"))) {
            return true;
        }

        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        if(sra == null) {
            return false;
        }

        HttpServletRequest request = sra.getRequest();
        if(request.getHeader("X-Real-IP") != null) {
            String ip = request.getHeader("X-Real-IP");
            return ip.startsWith("192.168");
        }

        if(request.getHeader("X-Forwarded-For") != null) {
            String ip = request.getHeader("X-Forwarded-For");
            return ip.startsWith("192.168");
        }

        return request.getRemoteAddr().startsWith("192.168");
    }

    @Override
    public void openCabinetDoor(Cabinet cabinet) {
        if(!isLocalNetworkRequest()) {
            logger.warn("NOT OPENING DOOR {} BECAUSE NOT LOCAL NETWORK", cabinet);
            return;
        }

        logger.warn("OPENING CABINET DOOR {}", cabinet);

        String cmdStr = "~/cabinet";
        if(cabinet == Cabinet.CABINET_6) {
            cmdStr += "6";
        } else if(cabinet == Cabinet.CABINET_7) {
            cmdStr += "7";
        } else {
            throw new IllegalArgumentException("You forgot to add the new cabinet here");
        }
        cmdStr += "Open.sh";

        executeCommandOnHost(cmdStr);
    }

    @Override
    public void openRoomDoor() {
        if(!isLocalNetworkRequest()) {
            logger.warn("NOT OPENING ROOM DOOR BECAUSE NOT LOCAL NETWORK");
            return;
        }

        logger.warn("OPENING ROOM DOOR");

        executeCommandOnHost("~/doorOpen.sh 3");
    }
}
