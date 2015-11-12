package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.model.user.Role;
import st.ilu.rms4csw.repository.RoleRepository;

import java.util.List;

/**
 * @author Mischa Holz
 */
@RestController("/api/v1/roles")
public class RoleController {

    private RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

}
