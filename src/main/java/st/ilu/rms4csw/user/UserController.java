package st.ilu.rms4csw.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
