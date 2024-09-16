package bankapp.bankApplication.controller;

import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.UserRegistrationRepository;
import bankapp.bankApplication.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/userRegistration")
public class UserRegistrationController {
    @Autowired
    UserRegistrationService userRegistrationService;


    @GetMapping("/find/{userName}")
    public ResponseEntity<UserRegistration> getById(@RequestBody RegistrationUpdateAllDTO userName) {
        Optional<UserRegistration> registration = userRegistrationService.getRegistrationByUserName(userName);
        return registration.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
