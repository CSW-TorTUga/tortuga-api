package st.ilu.rms4csw.repository.support;

import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.support.SupportMessage;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * @author Mischa Holz
 */
@Repository
public interface SupportMessageRepository extends JpaSpecificationRepository<SupportMessage, String> {
}
