package st.ilu.rms4csw.controller.base;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import st.ilu.rms4csw.Main;
import st.ilu.rms4csw.controller.base.exception.NotFoundException;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.patch.Patch;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mischa Holz
 */
public abstract class CrudController<T extends PersistentEntity> {

    protected JpaSpecificationRepository<T, String> repository;

    public abstract String getApiBase();

    private Class<T> entityClass;

    public CrudController(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    private Sort buildSortObject(HttpServletRequest request) {
        Sort.Direction dir = Sort.DEFAULT_DIRECTION;
        String dirValue = request.getParameter("direction");
        if(dirValue != null) {
            dir = Sort.Direction.fromString(dirValue);
        }

        String sortValue = request.getParameter("sort");
        if(sortValue == null) {
            return null;
        }

        return new Sort(dir, sortValue.split(","));
    }

    public List<T> findAll(HttpServletRequest request) {
        List<PersistentEntitySpecification<T>> specifications = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);

            String name = field.getName();

            String value = request.getParameter(name);
            if(value != null) {
                FilterCriteria criteria;
                if(value.startsWith("<")) {
                    criteria = new FilterCriteria();
                    criteria.setKey(name);
                    criteria.setOperation(FilterCriteria.Operation.LESS_THAN);
                    criteria.setValue(value.substring(1));
                } else if(value.startsWith(">")) {
                    criteria = new FilterCriteria();
                    criteria.setKey(name);
                    criteria.setOperation(FilterCriteria.Operation.GREATER_THAN);
                    criteria.setValue(value.substring(1));
                } else {
                    criteria = new FilterCriteria();
                    criteria.setKey(name);
                    criteria.setOperation(FilterCriteria.Operation.EQUALS);
                    criteria.setValue(value);
                }
                specifications.add(new PersistentEntitySpecification<>(criteria));
            }
        }

        if(specifications.size() > 0) {
            boolean first = true;
            Specifications<T> spec = null;
            for (PersistentEntitySpecification<T> specification : specifications) {
                if(first) {
                    spec = Specifications.where(specification);
                    first = false;
                } else {
                    spec = spec.and(specification);
                }
            }

            Sort sort = buildSortObject(request);
            if(sort == null) {
                return repository.findAll(spec);
            }
            return repository.findAll(spec, sort);
        }

        Sort sort = buildSortObject(request);
        if(sort == null) {
            return repository.findAll();
        }
        return repository.findAll(buildSortObject(request));
    }

    public T findOne(String id) {
        T ret = repository.findOne(id);
        if(ret == null) {
            throw new NotFoundException("Did not find resource with id '" + id + "'");
        }

        return ret;
    }

    public ResponseEntity<T> post(T newEntity, HttpServletResponse response) {
        if(repository.findOne(newEntity.getId()) != null) {
            throw new IllegalArgumentException("A resource with this ID exists already. Please use PUT and/or PATCH to update existing resources");
        }

        T ret = repository.save(newEntity);
        response.setHeader(HttpHeaders.LOCATION, Main.getApiBase() + "/" + getApiBase() + "/" + ret.getId());

        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    public T put(String id, T entity) {
        entity.setId(id);

        return repository.save(entity);
    }

    public ResponseEntity delete(String id) {
        repository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public T patch(String id, T entity) {
        T original = repository.findOne(id);
        if (original == null) {
            throw new NotFoundException("Did not find resource with the id " + id);
        }

        T patched = Patch.patch(original, entity);

        return repository.save(patched);
    }
}
