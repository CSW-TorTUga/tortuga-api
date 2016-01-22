package st.ilu.rms4csw.controller.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.base.AbstractCRUDCtrl;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Mischa Holz
 */
@RestController
@RequestMapping("/api/v1/" + UserController.USER_API_BASE)
public class UserController extends AbstractCRUDCtrl<User> {

    public final static String USER_API_BASE = "users";

    @Override
    public String getApiBase() {
        return USER_API_BASE;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll(HttpServletRequest request) {
        return super.findAll(request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User findOne(@PathVariable("id") String id) {
        return super.findOne(id);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> post(@RequestBody User user, HttpServletResponse response) {
        if(user.getRole() == Role.STUDENT) {
            Date expires = User.calculateNextSemesterEnd(new Date());
            user.setExpirationDate(Optional.of(expires));
        }

        if(user.getRole() == Role.ADMIN) {
            user.setExpirationDate(Optional.empty());
        }

        return super.post(user, response);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User put(@PathVariable("id") String id, @RequestBody User user) {
        User beforeUpdate = repository.findOne(id);
        if(beforeUpdate != null && user.getExpirationDate().isPresent() && !beforeUpdate.getExpirationDate().equals(user.getExpirationDate())) {
            throw new IllegalArgumentException("Can't set the expiration date of a user!");
        }

        return super.put(id, user);
    }

    @Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable("id") String id) {
        return super.delete(id);
	}

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public User patch(@PathVariable("id") String id, @RequestBody User user) {
        User beforeUpdate = repository.findOne(id);
        if(beforeUpdate != null && user.getExpirationDate().isPresent() && !beforeUpdate.getExpirationDate().equals(user.getExpirationDate())) {
            throw new IllegalArgumentException("Can't set the expiration date of a user!");
        }

        return super.patch(id, user);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.repository = userRepository;
    }
}
