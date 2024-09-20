package bankapp.bankApplication.controller;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.service.AccountHolderService;
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
    public AccountHolder create(@RequestBody AccountHolder accountHolder, @RequestParam String userRegistrationUserName, @RequestParam String userName) {
        try {
            return accountHolderService.create(accountHolder,userRegistrationUserName, userName);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/change")
    public ResponseEntity<AccountHolder> change (@RequestBody AccountHolder accountHolder , @RequestParam String userName) {
        try{
            Optional<AccountHolder> change = accountHolderService.change(accountHolder, userName);
            return change.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam String userName) throws UnauthorizedException {
        try {
            if (accountHolderService.delete(id, userName)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
