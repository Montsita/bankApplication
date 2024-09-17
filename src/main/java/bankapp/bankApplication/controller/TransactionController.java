package bankapp.bankApplication.controller;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //preguntar a Jarko
    @DeleteMapping("/{account_id}")
    public ResponseEntity<Void> delete(@PathVariable Long accountId)  {
        transactionService.delete(accountId);
        return ResponseEntity.noContent().build();
    }
}
