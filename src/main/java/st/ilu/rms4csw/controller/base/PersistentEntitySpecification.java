package st.ilu.rms4csw.controller.base;

import org.springframework.data.jpa.domain.Specification;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Mischa Holz
 */
public class PersistentEntitySpecification<T extends PersistentEntity> implements Specification<T> {

    private FilterCriteria criteria;

    public PersistentEntitySpecification(FilterCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation() == FilterCriteria.Operation.GREATER_THAN) {
            return builder.greaterThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation() == FilterCriteria.Operation.LESS_THAN) {
            return builder.lessThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation() == FilterCriteria.Operation.EQUALS) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.<String>get(criteria.getKey()), "" + criteria.getValue());
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
