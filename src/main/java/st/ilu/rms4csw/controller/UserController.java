package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import st.ilu.rms4csw.model.user.User;
import st.ilu.rms4csw.user.UserService;

/**
 * @author Mischa Holz
 */
//@Controller
@RequestMapping("/api/v1/users")
@ResponseBody
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/{id}")
    public User findOne(@PathVariable("id") String id) {
        return userService.findOne(id);
    }

}
