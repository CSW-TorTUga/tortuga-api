package st.ilu.rms4csw.repository.user;

import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * Created by hannes on 24.11.15.
 */
@Repository
public interface MajorRepository extends JpaSpecificationRepository<Major, String> {

}
