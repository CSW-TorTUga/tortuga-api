package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping
    private User postUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @RequestMapping("/{id}")
    private User putUser(@RequestParam String id, @RequestBody User user) {
        user.setId(id);

        return userRepository.save(user);
    }

//    private User patchUser(@RequestParam String id, @RequestBody )

}
