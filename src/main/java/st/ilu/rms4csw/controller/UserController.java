package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.UserRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@Controller
@RequestMapping("/api/v1/users")
@ResponseBody
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping
    private List<User> findAll() {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    private User postUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    private User putUser(@RequestParam String id, @RequestBody User user) {
        user.setId(id);

        return userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    private User patchUser(@RequestParam String id, @RequestBody User user) {
        return null;
    }

}
