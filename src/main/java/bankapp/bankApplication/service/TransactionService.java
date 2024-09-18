package bankapp.bankApplication.service;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.AdminNotFoundException;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;


    public List<Transaction> getAll() { return transactionRepository.findAll(); }

    public Optional<Transaction> getById(Long id){ return transactionRepository.findById(id); }

    public List<Transaction> getByAccountId(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }

    public void delete(Long accountId) {
        transactionRepository.deleteByAccountId(accountId);
    }


}