package st.ilu.rms4csw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import st.ilu.rms4csw.model.user.Role;

/**
 * @author Mischa Holz
 */
public interface RoleRepository extends JpaRepository<Role, String> {
}
