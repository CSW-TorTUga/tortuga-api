package st.ilu.rms4csw.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Mischa Holz
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        String url = System.getenv("DATABASE_URL");
        if(url == null) {
            // default. if you need a different configuration for development just
            // set the environment variable in your runner using the same format as
            // below
            url = "postgres://postgres:password@localhost:5432/rms4csw";
        }

        URI dbUri = new URI(url);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }

}
