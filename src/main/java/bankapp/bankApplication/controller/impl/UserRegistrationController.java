package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.registrations.UserRegistration;

import bankapp.bankApplication.service.impl.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userRegistration")
public class UserRegistrationController {
    @Autowired
    UserRegistrationService userRegistrationService;

    @GetMapping
    public List<UserRegistration> getAll() {
        return userRegistrationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRegistration> getById(@PathVariable Long id) {
        Optional<UserRegistration> userRegistration = userRegistrationService.getById(id);
        return userRegistration.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/find/{userName}")
    public ResponseEntity<UserRegistration> getById(@PathVariable String userName) {
        Optional<UserRegistration> registration = userRegistrationService.getRegistrationByUserName(userName);
        return registration.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public UserRegistration create(@RequestBody UserRegistration userRegistration) {
        try {
            return userRegistrationService.create(userRegistration);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserRegistration> getUsers() {
        return userRegistrationService.getUsers();
    }

    /**
     * Save a new user
     *
     * @param user the user to be saved
     */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveUser(@RequestBody UserRegistration user) {
        userRegistrationService.saveUser(user);
    }
}
