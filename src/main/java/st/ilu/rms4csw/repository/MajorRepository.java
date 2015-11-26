package st.ilu.rms4csw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.model.user.User;

/**
 * Created by hannes on 24.11.15.
 */
public interface MajorRepository extends JpaRepository<Major, String> {

}
