package bankapp.bankApplication.service;
import bankapp.bankApplication.exception.UnauthorizedException;
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

    @Autowired
    private UserRegistrationService userRegistrationService;


    public List<Transaction> getAll() { return transactionRepository.findAll(); }

    public Optional<Transaction> getById(Long id){ return transactionRepository.findById(id); }

    public Transaction create(Transaction transaction , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return transactionRepository.save(transaction);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create transaction.");
        }
    }
    public Optional<Transaction> change(Transaction transaction , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (transactionRepository.existsById(transaction.getId())) {
                return Optional.of(transactionRepository.save(transaction));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change transaction.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(transactionRepository.existsById(id)){
                transactionRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete transaction.");
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