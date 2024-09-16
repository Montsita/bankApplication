package bankapp.bankApplication.controller;

import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Optional<Account> account = accountService.getById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/account")
    public Account create(@RequestBody Account account, @RequestBody RegistrationUpdateAllDTO registrationUserName) {
        try {
            return accountService.create(account, registrationUserName);

        } catch (UnauthorizedException e) {
            return null;
        }

    }


}





   // @PostMapping
   // public Account createAccount(@RequestBody Account account){ return accountService.createAccount(account);}
    //IMPORTANTE QUE LO PRIMERO QUE PIDA SEA EL TYPEACCOUNT, para que le de valores iniciales y si el usuario nos da un
    // valor que se sobrescriban con los valores del usuario



