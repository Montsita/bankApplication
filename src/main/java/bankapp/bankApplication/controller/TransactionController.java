package bankapp.bankApplication.controller;

import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
