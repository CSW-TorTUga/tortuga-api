package st.ilu.rms4csw.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.user.User;

/**
 * @author Mischa Holz
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findOneByLoginName(@Param("loginName") String loginName);

}
