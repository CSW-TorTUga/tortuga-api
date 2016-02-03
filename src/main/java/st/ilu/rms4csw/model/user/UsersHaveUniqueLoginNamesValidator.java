package st.ilu.rms4csw.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import st.ilu.rms4csw.repository.user.UserRepository;
import st.ilu.rms4csw.util.SpringInjectedValidator;

import javax.validation.ConstraintValidatorContext;

/**
 * @author Mischa Holz
 */
@Component
public class UsersHaveUniqueLoginNamesValidator extends SpringInjectedValidator<UsersHaveUniqueLoginNames, User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean _isValid(User value, ConstraintValidatorContext context) {
        User candidate = userRepository.findOneByLoginName(value.getLoginName());

        if(candidate == null) {
            return true;
        }

        boolean valid = candidate.getId().equals(value.getId());
        if(!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Dieser Loginname wird bereits von einem anderen Benutzer verwendet.")
                    .addPropertyNode("loginName").addConstraintViolation();
        }

        return valid;
    }
}
