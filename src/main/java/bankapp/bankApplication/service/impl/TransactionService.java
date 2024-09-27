package bankapp.bankApplication.service.impl;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.transactions.Transaction;
import bankapp.bankApplication.repository.impl.TransactionRepository;
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

    public Transaction create(Transaction transaction ) throws UnauthorizedException {
        return transactionRepository.save(transaction);
    }
    public Optional<Transaction> change(Transaction transaction ) throws UnauthorizedException {
        if (transactionRepository.existsById(transaction.getId())) {
            return Optional.of(transactionRepository.save(transaction));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(transactionRepository.existsById(id)){
            transactionRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
    public void deleteByAccountId(Long accountId) {
        transactionRepository.deleteByAccountId(accountId);
    }
    public List<Transaction> getByAccountId(Long accountId){
        return transactionRepository.findByAccountId(accountId);
    }



    public Transaction  getLastTransaccion(Long accountId){
        return transactionRepository.findLastTransactionByAccountId(accountId);
    }

}