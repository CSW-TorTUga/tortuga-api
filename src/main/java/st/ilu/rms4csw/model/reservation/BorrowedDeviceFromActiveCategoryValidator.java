package st.ilu.rms4csw.model.reservation;

import st.ilu.rms4csw.model.device.Device;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Mischa Holz
 */
public class BorrowedDeviceFromActiveCategoryValidator implements ConstraintValidator<BorrowedDeviceFromActiveCategory, Device> {
    @Override
    public void initialize(BorrowedDeviceFromActiveCategory constraintAnnotation) {

    }

    @Override
    public boolean isValid(Device value, ConstraintValidatorContext context) {
        return value.getCategory().isActive();
    }
}
