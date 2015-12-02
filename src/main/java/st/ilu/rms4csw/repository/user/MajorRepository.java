package st.ilu.rms4csw.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.major.Major;

/**
 * Created by hannes on 24.11.15.
 */
@Repository
public interface MajorRepository extends JpaRepository<Major, String> {

}
