package bankapp.bankApplication.service;
import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.AdminNotFoundException;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.AccountRepository;
import bankapp.bankApplication.repository.AdminRepository;
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

    public List<Account> getAll() { return accountRepository.findAll(); }

    public Optional<Account> getById(Long id){ return accountRepository.findById(id);}


    public Account create(Account account, String userName) throws UnauthorizedException {
        // Verifica si el admin existe en el repositorio

        Optional<UserRegistration> regOptional = userRegistrationService.getRegistrationByUserName(userName);

        if (!regOptional.isPresent()) {
            throw new AdminNotFoundException("User with userName " + userName+ " not found");
        }

        UserRegistration reg = regOptional.get();

        // Verifica si el tipo de usuario es ADMIN
        if (reg.getType() != UserType.ADMIN) {
            throw new UnauthorizedException("Only ADMIN users can create accounts.");
        }

        // Verifica si el administrador existe en el repositorio
        if (!adminRepository.existsById(reg.getUser().getId())) {
            throw new AdminNotFoundException("Admin with userName " + reg.getUserName() + " not found");
        }

        // Si el administrador existe, guarda y devuelve la cuenta
        return accountRepository.save(account);
    }
}
