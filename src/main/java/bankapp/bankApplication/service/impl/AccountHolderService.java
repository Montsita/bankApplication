package bankapp.bankApplication.service.impl;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.AccountHolder;
import bankapp.bankApplication.model.registrations.UserRegistration;
import bankapp.bankApplication.repository.impl.AccountHolderRepository;
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

    public AccountHolder create(AccountHolder accountHolder , String accountHolderUserName) throws UnauthorizedException {
        UserRegistration userRegistration =new UserRegistration();
        userRegistration.setUsername(accountHolderUserName);
        userRegistration=userRegistrationService.create(userRegistration);
        accountHolder.setUserRegistration(userRegistration);
        return accountHolderRepository.save(accountHolder);
    }
    public Optional<AccountHolder> change(AccountHolder accountHolder ) throws UnauthorizedException {
        if (accountHolderRepository.existsById(accountHolder.getId())) {
            return Optional.of(accountHolderRepository.save(accountHolder));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(accountHolderRepository.existsById(id)){
            accountHolderRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
