package bankapp.bankApplication.controller.impl;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.AccountHolder;
import bankapp.bankApplication.service.impl.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accountHolder")
public class AccountHolderController {
    @Autowired
    private AccountHolderService accountHolderService;

    @GetMapping
    public List<AccountHolder> getAll(){ return accountHolderService.getAll();}

    @GetMapping("/{id}")
    public ResponseEntity<AccountHolder> getById(@PathVariable Long id){
        Optional<AccountHolder> accountHolder = accountHolderService.getById(id);
        return accountHolder.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public AccountHolder create(@RequestBody AccountHolder accountHolder, @RequestParam String userRegistrationUserName) {
        try {
            return accountHolderService.create(accountHolder,userRegistrationUserName);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change")
    public ResponseEntity<AccountHolder> change (@RequestBody AccountHolder accountHolder) {
        try{
            Optional<AccountHolder> change = accountHolderService.change(accountHolder);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UnauthorizedException {
        try {
            if (accountHolderService.delete(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
