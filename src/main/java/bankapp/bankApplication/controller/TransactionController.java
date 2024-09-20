package bankapp.bankApplication.controller;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll(){ return transactionService.getAll();}

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id){
        Optional<Transaction> transaction = transactionService.getById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getByAccountId(@PathVariable Long accountId){
        return transactionService.getByAccountId(accountId);
    }

    @GetMapping("/last/{accountId}")
    public Transaction getLastTransaction(@PathVariable Long accountId){
        return transactionService.getLastTransaccion(accountId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestParam String userName) throws UnauthorizedException {
        try {
            if (transactionService.delete(id, userName)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<Void> delete(@PathVariable Long accountId)  {
        transactionService.deleteByAccountId(accountId);
        return ResponseEntity.noContent().build();
    }

}
