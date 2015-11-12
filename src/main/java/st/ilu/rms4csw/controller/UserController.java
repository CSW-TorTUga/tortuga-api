package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.controller.exception.NotFoundException;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.repository.UserRepository;
import st.ilu.rms4csw.util.Patch;

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
    private User putUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id);

        return userRepository.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    private User patchUser(@PathVariable String id, @RequestBody User user) {
        User original = userRepository.findOne(id);
        if(original == null) {
            throw new NotFoundException("Did not find user with the id " + id);
        }

        User patchedUser = Patch.patch(original, user);

        return userRepository.save(patchedUser);
    }

}
