package st.ilu.rms4csw.model.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;
import st.ilu.rms4csw.model.base.PersistentEntity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import java.util.Date;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SupportMessage extends PersistentEntity {

    @NotEmpty(message = "Support Anfragen brauchen einen Betreff")
    private String subject;

    @NotEmpty(message = "Support Anfragen brauchen einen Text")
    private String body;

    @Access(AccessType.FIELD)
    private String email;

    @Access(AccessType.FIELD)
    private String name;

    private Boolean done = false;

    private Date openedAt;

    @Access(AccessType.FIELD)
    private String answer;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(this.email);
    }

    public void setEmail(Optional<String> email) {
        this.email = email.orElse(null);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public void setName(Optional<String> name) {
        this.name = name.orElse(null);
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Optional<String> getAnswer() {
        return Optional.ofNullable(this.answer);
    }

    public void setAnswer(Optional<String> answer) {
        this.answer = answer.orElse(null);
    }

    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }
}