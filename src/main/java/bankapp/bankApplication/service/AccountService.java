package bankapp.bankApplication.service;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.AdminNotFoundException;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.AccountRepository;
import bankapp.bankApplication.repository.AdminRepository;
import bankapp.bankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public Account create(Account account, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return accountRepository.save(account);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create accounts.");
        }
    }

    public Optional<Account> change(Account account, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (accountRepository.existsById(account.getId())) {
                return Optional.of(accountRepository.save(account));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change accounts.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(accountRepository.existsById(id)){
                accountRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete accounts.");
        }
    }


}