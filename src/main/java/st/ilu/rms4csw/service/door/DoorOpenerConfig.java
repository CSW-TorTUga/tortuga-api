package st.ilu.rms4csw.service.door;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import st.ilu.rms4csw.model.cabinet.Cabinet;

/**
 * @author Mischa Holz
 */
@Configuration
public class DoorOpenerConfig {

    private static Logger logger = LoggerFactory.getLogger(DoorOpener.class);

    @Bean
    public static DoorOpener doorOpener(@Value("${SSH_DOOR_HOST:NO_HOST}") String sshHost,
                                        @Value("${SSH_DOOR_USER:NO_USER}") String sshUser,
                                        @Value("${SSH_DOOR_PASSWORD:NO_PASSWORD}") String sshPassword,
                                        @Value("${SSH_FINGER_PRINT:PRINT}") String fingerPrint) {
        if(sshHost != null && !sshHost.equals("NO_HOST")) {
            return new SSHDoorOpener(sshHost, sshUser, sshPassword, fingerPrint);
        } else {
            return new DoorOpener() {
                @Override
                public void openCabinetDoor(Cabinet cabinet) {
                    logger.warn("OPENING DOOR FOR {} (FAKE)", cabinet);
                }

                @Override
                public void openRoomDoor() {
                    logger.warn("OPENING ROOM DOOR (FAKE)");
                }

                @Override
                public void openRoomDoorWithoutCheckingNetwork() {
                    logger.warn("OPENING ROOM DOOR (FAKE)");
                }
            };
        }
    }

}
