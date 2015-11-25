package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.Main;
import st.ilu.rms4csw.controller.exception.NotFoundException;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.UserRepository;
import st.ilu.rms4csw.util.Patch;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Mischa Holz
 */
@Controller
@RequestMapping("/api/v1/" + UserController.USER_API_BASE)
@ResponseBody
public class UserController {

    public final static String USER_API_BASE = "users";

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping
    @PreAuthorize("hasAuthority('OP_TEST')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> postUser(@RequestBody User user, HttpServletResponse response) {
        User ret = userRepository.save(user);
        response.setHeader("Location", Main.getApiBase() + USER_API_BASE + "/" + ret.getId());

        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User putUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);

        return userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public User patchUser(@PathVariable String id, @RequestBody User user) {
        User original = userRepository.findOne(id);
        if (original == null) {
            throw new NotFoundException("Did not find user with the id " + id);
        }

        User patchedUser = Patch.patch(original, user);

        return userRepository.save(patchedUser);
    }

}
