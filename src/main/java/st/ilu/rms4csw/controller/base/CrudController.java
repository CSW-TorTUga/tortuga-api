package st.ilu.rms4csw.controller.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import st.ilu.rms4csw.Main;
import st.ilu.rms4csw.controller.exception.NotFoundException;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.patch.Patch;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
public abstract class CrudController<T extends PersistentEntity> {

    protected JpaRepository<T, String> repository;

    public abstract String getApiBase();

    public List<T> findAll() {
        return repository.findAll();
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
        response.setHeader(HttpHeaders.LOCATION, Main.getApiBase() + getApiBase() + "/" + ret.getId());

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
