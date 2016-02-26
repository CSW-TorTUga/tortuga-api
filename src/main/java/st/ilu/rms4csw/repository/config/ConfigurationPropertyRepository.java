package st.ilu.rms4csw.repository.config;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import st.ilu.rms4csw.model.config.ConfigurationProperty;
import st.ilu.rms4csw.repository.base.JpaSpecificationRepository;

/**
 * @author Mischa Holz
 */
@Repository
public interface ConfigurationPropertyRepository extends JpaSpecificationRepository<ConfigurationProperty, String> {

    ConfigurationProperty findOneByLabel(@Param("label") String label);

}
