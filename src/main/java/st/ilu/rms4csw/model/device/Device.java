package st.ilu.rms4csw.model.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.model.cabinet.Cabinet;
import st.ilu.rms4csw.model.devicecategory.DeviceCategory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Device extends PersistentEntity {

    @NotEmpty
    @NotNull
    private String name;

    @OneToOne
    private DeviceCategory deviceCategory;

    private String description;

    private String accessories;

    @Enumerated(EnumType.STRING)
    @NotEmpty
    @NotNull
    private Cabinet cabinet;

    @NotEmpty
    @NotNull
    private String inventoryNumber;

    @Access(AccessType.FIELD)
    private Date buyDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceCategory getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(DeviceCategory deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public Optional<Date> getBuyDate() {
        return Optional.ofNullable(buyDate);
    }

    public void setBuyDate(Optional<Date> buyDate) {
        this.buyDate = buyDate.orElse(null);
    }
}
