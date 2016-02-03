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
public class UsersHaveUniqueLoginNamesValidator extends SpringInjectedValidator<UsersHaveUniqueLoginNames, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean _isValid(String value, ConstraintValidatorContext context) {
        return userRepository.findOneByLoginName(value) == null;
    }
}
