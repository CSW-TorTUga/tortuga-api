package st.ilu.rms4csw.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Mischa Holz
 */
public enum Role {

    ADMIN("OP_TEST"),
    ;

    private String[] privileges;

    Role(String... privileges) {
        this.privileges = privileges;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> ret = new HashSet<>();
        ret.add(new SimpleGrantedAuthority("ROLE_" + this.toString()));

        ret.addAll(Arrays.stream(this.privileges).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        return ret;
    }
}
