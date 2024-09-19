package bankapp.bankApplication.controller;

import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create")
    public Account create(@RequestBody Account account, @RequestParam String userName) {
        try {
            return accountService.create(account, userName);

        } catch (UnauthorizedException e) {
            return null;
        }

    }

    @PutMapping("/change")
    public ResponseEntity<Account> change (@RequestBody Account account, @RequestParam String userName) {
        try{
            Optional<Account> change = accountService.change(account, userName);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam String userName) throws UnauthorizedException {
        if(accountService.delete(id, userName)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}




