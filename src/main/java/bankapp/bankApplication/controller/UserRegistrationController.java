package bankapp.bankApplication.controller;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.service.UserRegistrationService;
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
    public UserRegistration create(@RequestBody UserRegistration userRegistration,  @RequestParam String userName) {
        try {
            return userRegistrationService.create(userRegistration, userName);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
