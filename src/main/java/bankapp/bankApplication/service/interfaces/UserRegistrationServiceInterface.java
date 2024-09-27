package bankapp.bankApplication.service.interfaces;

import bankapp.bankApplication.model.registrations.Role;
import bankapp.bankApplication.model.registrations.UserRegistration;

import java.util.List;
import java.util.Optional;

/**
 * The UserServiceInterface is an interface that defines the methods that are available to perform operations on User entities.
 */
public interface UserRegistrationServiceInterface {

    /**
     * This method is used to save a User entity to the database.
     *
     * @param user the User entity to be saved.
     * @return the saved User entity.
     */
    UserRegistration saveUser(UserRegistration user);

    /**
     * This method is used to save a Role entity to the database.
     *
     * @param role the Role entity to be saved.
     * @return the saved Role entity.
     */
    Role saveRole(Role role);

    /**
     * This method is used to add a Role to a User.
     *
     * @param username the username of the User to which the Role is to be added.
     * @param roleName the name of the Role to be added.
     */
    void addRoleToUser(String username, String roleName);

    /**
     * This method is used to retrieve a User from the database by its username.
     *
     * @param username the username of the User to be retrieved.
     * @return the retrieved User entity.
     */
    Optional<UserRegistration> getUser(String username);

    /**
     * This method is used to retrieve all User entities from the database.
     *
     * @return a List of all User entities.
     */
    List<UserRegistration> getUsers();
}

