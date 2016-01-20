package st.ilu.rms4csw.util;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Mischa Holz
 */
public abstract class SpringInjectedValidator<ANNOTATION extends Annotation, MODEL> implements ConstraintValidator<ANNOTATION, MODEL> {
    @Override
    public void initialize(ANNOTATION constraintAnnotation) {
        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                try {
                    field.set(this, ApplicationContextProvider.getContext().getBean(field.getType()));
                } catch(IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
