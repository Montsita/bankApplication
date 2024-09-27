package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.dto.AccountPasswordUpdateDTO;
import bankapp.bankApplication.exception.PasswordNotAvailable;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.accounts.Account;
import bankapp.bankApplication.model.transactions.Transaction;
import bankapp.bankApplication.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    public Account create(@RequestBody Account account) {
        try {
            return accountService.create(account);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change")
    public ResponseEntity<Account> change (@RequestBody Account account) {
        try{
            Optional<Account> change = accountService.change(account);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws UnauthorizedException {
        try {
            if (accountService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/createMovement/{id}")
    public Transaction createMovement(@PathVariable Long id, @RequestParam BigDecimal amount){
        return accountService.createMovement(id,amount);
    }

    @PostMapping("/createTransfer/{id}")
    public Transaction createTransfer(@PathVariable Long id, @RequestParam BigDecimal amount, Long destinyId, String concept){
        return accountService.createTransfer(id,amount,destinyId,concept);
    }

    @PatchMapping("/changePassword/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Validated @RequestBody AccountPasswordUpdateDTO account){
        try {
            if (accountService.changePassword(id,account)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (PasswordNotAvailable e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}




