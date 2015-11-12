package st.ilu.rms4csw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import st.ilu.rms4csw.model.user.User;

/**
 * @author Mischa Holz
 */
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, String> {
}
