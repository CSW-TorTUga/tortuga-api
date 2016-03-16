package st.ilu.rms4csw.repository.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Repository
public interface UserRepository extends JpaSpecificationRepository<User, String> {

    User findOneByLoginName(@Param("loginName") String loginName);

    User findOneByEmail(@Param("email") String email);

    List<User> findAllByMajorId(String majorId);

}
