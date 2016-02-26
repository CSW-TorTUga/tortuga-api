package st.ilu.rms4csw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class Main {

    public static String getApiBase() {
        String envBase = System.getenv("RMS_CSW_API_BASE");
        if(envBase != null) {
            return envBase;
        }

        return "http://localhost:7081/api/v1";
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
