package st.ilu.rms4csw.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import st.ilu.rms4csw.Main;
import st.ilu.rms4csw.controller.base.exception.NotFoundException;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.patch.Patch;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;
import st.ilu.rms4csw.service.PersistentEntityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mischa Holz
 */
public abstract class AbstractCRUDCtrl<T extends PersistentEntity> {

    protected JpaSpecificationRepository<T, String> repository;

    private PersistentEntityService persistentEntityService;

    public abstract String getApiBase();

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

    public List<T> findAll() {
        return repository.findAll();
    }

    public List<T> findAll(HttpServletRequest request) {
        return findAll(request.getParameterMap(), buildSortObject(request));
    }

    public List<T> findAll(Map<String, String[]> params, Sort sort) {
        params = new HashMap<>(params);
        params.remove("direction");
        params.remove("sort");

        return persistentEntityService.findAll(params, sort, repository);
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
        if(original == null) {
            throw new NotFoundException("Did not find resource with the id " + id);
        }

        T patched = Patch.patch(original, entity);

        return repository.save(patched);
    }

    @Autowired
    public void setPersistentEntityService(PersistentEntityService persistentEntityService) {
        this.persistentEntityService = persistentEntityService;
    }

    @Autowired
    public void setRepository(JpaSpecificationRepository<T, String> repository) {
        this.repository = repository;
    }
}
