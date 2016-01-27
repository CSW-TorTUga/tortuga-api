package st.ilu.rms4csw.service.door;

import st.ilu.rms4csw.model.cabinet.Cabinet;

/**
 * @author Mischa Holz
 */
public interface DoorOpener {

    void openCabinetDoor(Cabinet cabinet);

    void openRoomDoor();

}
