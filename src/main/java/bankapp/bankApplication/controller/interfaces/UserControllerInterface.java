package bankapp.bankApplication.controller.interfaces;


import bankapp.bankApplication.model.registrations.UserRegistration;

import java.util.List;

/**
 * Interface for UserController. Contains methods for handling user related operations
 */
public interface UserControllerInterface {
    /**
     * Retrieves a list of all users
     *
     * @return list of all users
     */
    List<UserRegistration> getUsers();

    /**
     * Saves a new user
     *
     * @param user the user to be saved
     * @return the saved user
     */
    UserRegistration saveUser(UserRegistration user);
}
