package st.ilu.rms4csw.security.token;

import st.ilu.rms4csw.model.user.Role;

import java.util.Date;

/**
 * @author Mischa Holz
 */
public class Token {

    private String loginName;

    private String id;

    private Date expires;

    private Role role;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
