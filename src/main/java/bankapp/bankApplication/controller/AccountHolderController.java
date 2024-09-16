package bankapp.bankApplication.controller;

import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.service.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
