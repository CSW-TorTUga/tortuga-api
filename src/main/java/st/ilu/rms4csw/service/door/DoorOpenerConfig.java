package st.ilu.rms4csw.service.door;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import st.ilu.rms4csw.model.cabinet.Cabinet;

/**
 * @author Mischa Holz
 */
@Configuration
public class DoorOpenerConfig {

    @Bean
    public static DoorOpener doorOpener(@Value("${SSH_DOOR_HOST:NO_HOST}") String sshHost,
                                        @Value("${SSH_DOOR_USER:NO_USER}") String sshUser,
                                        @Value("${SSH_DOOR_PASSWORD:NO_PASSWORD}") String sshPassword) {
        if(sshHost != null && !sshHost.equals("NO_HOST")) {
            return new SSHDoorOpener(sshHost, sshUser, sshPassword);
        } else {
            return new DoorOpener() {
                @Override
                public void openCabinetDoor(Cabinet cabinet) {}

                @Override
                public void openRoomDoor() {}
            };
        }
    }

}
