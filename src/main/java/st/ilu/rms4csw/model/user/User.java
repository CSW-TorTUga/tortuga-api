package st.ilu.rms4csw.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import st.ilu.rms4csw.model.base.PersistentEntity;
import st.ilu.rms4csw.model.major.Major;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity(name = "rms_user")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class User extends PersistentEntity {

    @Column(unique = true)
    private String loginName;

    private String firstName;

    private String lastName;

    private String email;

    @Access(AccessType.FIELD)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    @Access(AccessType.FIELD)
    private Major major;

    @Access(AccessType.FIELD)
    private Long studentId;

    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Access(AccessType.FIELD)
    private Date expirationDate;

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

    public Optional<Major> getMajor() {
        return Optional.ofNullable(this.major);
    }

    public void setMajor(Optional<Major> major) {
        if(major.isPresent()) {
            this.major = major.get();
        } else {
            this.major = null;
        }
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Optional<Date> getExpirationDate() {
        if(expirationDate == null) {
            return Optional.empty();
        }

        return Optional.of(expirationDate);
    }

    public void setExpirationDate(Optional<Date> expirationDate) {
        if(expirationDate.isPresent()) {
            this.expirationDate = expirationDate.get();
        } else {
            this.expirationDate = null;
        }
    }

    public static Date calculateNextSemesterEnd(Date from) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        if(calendar.get(Calendar.MONTH) < Calendar.APRIL) {
            calendar.set(Calendar.MONTH, Calendar.APRIL);
        } else if(calendar.get(Calendar.MONTH) < Calendar.OCTOBER) {
            calendar.set(Calendar.MONTH, Calendar.OCTOBER);
        } else {
            int year = calendar.get(Calendar.YEAR);
            year++;
            calendar.set(Calendar.YEAR, year);

            calendar.set(Calendar.MONTH, Calendar.APRIL);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
