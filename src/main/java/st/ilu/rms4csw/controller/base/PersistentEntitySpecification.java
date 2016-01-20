package st.ilu.rms4csw.controller.base;

import org.springframework.data.jpa.domain.Specification;
import st.ilu.rms4csw.controller.base.exception.IllegalFilterException;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.criteria.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Mischa Holz
 */
public class PersistentEntitySpecification<T extends PersistentEntity> implements Specification<T> {

    private FilterCriteria criteria;

    public PersistentEntitySpecification(FilterCriteria criteria) {
        this.criteria = criteria;
    }

    private Path getPath(Path root, String key) {
        Path path = root;
        String[] strPath = key.split("\\.");

        int i = 0;
        try {
            for(; i < strPath.length; i++) {
                path = path.get(strPath[i]);
            }

            Class nodeType = path.getJavaType();

            if(PersistentEntity.class.isAssignableFrom(nodeType)) {
                path = path.get("id");
            }
        } catch(IllegalArgumentException e) {
            String type = (i - 1 >= 0) ? strPath[i - 1] : "";
            String field = strPath[i];

            throw new IllegalFilterException(type, field);
        }


        return path;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path path = getPath(root, criteria.getKey());

        if(path.getJavaType().equals(Date.class)) {
            Date date = new Date(Long.parseLong((String) criteria.getValue()));

            if(criteria.getOperation() == FilterCriteria.Operation.GREATER_THAN) {
                return builder.greaterThanOrEqualTo(path, date);
            } else if(criteria.getOperation() == FilterCriteria.Operation.LESS_THAN) {
                return builder.lessThanOrEqualTo(path, date);
            } else if(criteria.getOperation() == FilterCriteria.Operation.EQUALS) {
                return builder.equal(path, date);
            }
            return null;
        }
        if(criteria.getOperation() == FilterCriteria.Operation.GREATER_THAN) {
            return builder.greaterThanOrEqualTo(path, criteria.getValue().toString());
        } else if(criteria.getOperation() == FilterCriteria.Operation.LESS_THAN) {
            return builder.lessThanOrEqualTo(path, criteria.getValue().toString());
        } else if(criteria.getOperation() == FilterCriteria.Operation.EQUALS) {
            if(path.getJavaType() == String.class) {
                return builder.like(path, "" + criteria.getValue());
            } else {
                return builder.equal(path, criteria.getValue());
            }
        }
        return null;
    }
}
