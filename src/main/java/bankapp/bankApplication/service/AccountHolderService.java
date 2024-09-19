package bankapp.bankApplication.service;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.repository.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderService {
    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    public List<AccountHolder> getAll() { return accountHolderRepository.findAll(); }

    public Optional<AccountHolder> getById(Long id){
        Optional<AccountHolder> accountHolder=accountHolderRepository.findById(id);
        if (accountHolder.isPresent()) {
            return accountHolderRepository.findById(id);
        }else{
            return Optional.empty();
        }
    }

    public AccountHolder create(AccountHolder accountHolder , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return accountHolderRepository.save(accountHolder);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create accountHolder.");
        }
    }
    public Optional<AccountHolder> change(AccountHolder accountHolder , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (accountHolderRepository.existsById(accountHolder.getId())) {
                return Optional.of(accountHolderRepository.save(accountHolder));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change accountHolder.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(accountHolderRepository.existsById(id)){
                accountHolderRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete accountHolder.");
        }
    }
}
