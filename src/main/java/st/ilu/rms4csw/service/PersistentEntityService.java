package st.ilu.rms4csw.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import st.ilu.rms4csw.controller.base.FilterCriteria;
import st.ilu.rms4csw.controller.base.PersistentEntitySpecification;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mischa Holz
 */
@Service
public class PersistentEntityService {

    public <T extends PersistentEntity> List<T> findAll(Map<String, String[]> params, Sort sort, JpaSpecificationRepository<T, String> repository) {
        List<PersistentEntitySpecification<T>> specifications = new ArrayList<>();

        params.forEach((key, valueArray) -> {
            for(String value : valueArray) {
                FilterCriteria criteria;
                if(value.startsWith("<")) {
                    criteria = new FilterCriteria();
                    criteria.setKey(key);
                    criteria.setOperation(FilterCriteria.Operation.LESS_THAN);
                    criteria.setValue(value.substring(1));
                } else if(value.startsWith(">")) {
                    criteria = new FilterCriteria();
                    criteria.setKey(key);
                    criteria.setOperation(FilterCriteria.Operation.GREATER_THAN);
                    criteria.setValue(value.substring(1));
                } else {
                    criteria = new FilterCriteria();
                    criteria.setKey(key);
                    criteria.setOperation(FilterCriteria.Operation.EQUALS);
                    criteria.setValue(value);
                }
                specifications.add(new PersistentEntitySpecification<>(criteria));
            }
        });

        if(specifications.size() > 0) {
            boolean first = true;
            Specifications<T> spec = null;
            for(PersistentEntitySpecification<T> specification : specifications) {
                if(first) {
                    spec = Specifications.where(specification);
                    first = false;
                } else {
                    spec = spec.and(specification);
                }
            }

            if(sort == null) {
                try {
                    return repository.findAll(spec);
                } catch(InvalidDataAccessApiUsageException e) {
                    throw (RuntimeException) e.getCause();
                }
            }
            try {
                return repository.findAll(spec, sort);
            } catch(InvalidDataAccessApiUsageException e) {
                throw (RuntimeException) e.getCause();
            }
        }

        if(sort == null) {
            return repository.findAll();
        }
        return repository.findAll(sort);
    }
}
