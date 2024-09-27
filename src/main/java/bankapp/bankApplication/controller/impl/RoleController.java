package bankapp.bankApplication.controller.impl;


import bankapp.bankApplication.controller.interfaces.RoleControllerInterface;
import bankapp.bankApplication.dto.RoleToUserDTO;
import bankapp.bankApplication.model.registrations.Role;
import bankapp.bankApplication.service.interfaces.UserRegistrationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * RESTful API for Role management
 */
@RestController
@RequestMapping("/role")
public class RoleController implements RoleControllerInterface {

    /**
     * User service for accessing user data
     */
    @Autowired
    private UserRegistrationServiceInterface userRegistrationService;

    /**
     * Save a new role
     *
     * @param role role to be saved
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRole(@RequestBody Role role) {
        userRegistrationService.saveRole(role);
    }

    /**
     * Add a role to a user
     *
     * @param roleToUserDTO DTO containing the username and role name
     */
    @PostMapping("/addToUser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addRoleToUser(@RequestBody RoleToUserDTO roleToUserDTO) {
        userRegistrationService.addRoleToUser(roleToUserDTO.getUsername(), roleToUserDTO.getRoleName());
    }
}
