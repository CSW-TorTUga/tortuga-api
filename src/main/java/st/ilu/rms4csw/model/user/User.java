package st.ilu.rms4csw.model.user;

import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity(name = "rms_user")
public class User extends PersistentEntity {

    private String loginName;

    private String firstName;

    private String lastName;

    private String email;

    @Access(AccessType.FIELD)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Major major;

    @Access(AccessType.FIELD)
    private Long studentId;

    private String phoneNumber;

    private String password;

    @OneToOne
    private Role role;

    private Date expires;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<Gender> getGender() {
        if(this.gender == null) {
            return Optional.empty();
        }

        return Optional.of(this.gender);
    }

    public void setGender(Optional<Gender> gender) {
        if(gender.isPresent()) {
            this.gender = gender.get();
        } else {
            this.gender = null;
        }
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Optional<Long> getStudentId() {
        if(studentId == null) {
            return Optional.empty();
        }

        return Optional.of(studentId);
    }

    public void setStudentId(Optional<Long> studentId) {
        if(studentId.isPresent()) {
            this.studentId = studentId.get();
        } else {
            this.studentId = null;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
