package bankapp.bankApplication.service;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAll() { return transactionRepository.findAll(); }

    public Optional<Transaction> getById(Long id){ return transactionRepository.findById(id); }

}