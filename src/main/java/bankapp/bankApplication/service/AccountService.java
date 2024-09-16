package bankapp.bankApplication.service;
import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.AdminNotFoundException;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.AccountHolderRepository;
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
    private AdminRepository adminRepository;
    private UserRegistrationService userRegistrationService;

    public List<Account> getAll() { return accountRepository.findAll(); }

    public Optional<Account> getById(Long id){ return accountRepository.findById(id); }

    public Account create(Account account, RegistrationUpdateAllDTO registration) throws UnauthorizedException {
        // Verifica si el admin existe en el repositorio
        UserRegistration reg = userRegistrationService.getRegistrationByUserName(registration);
        if (reg == null) {
            throw new AdminNotFoundException("User with userName " + registration.getUsername() + " not found");
        }

        // Verifica si el tipo de usuario es ADMIN
        if (reg.getType() != UserType.ADMIN) {
            throw new UnauthorizedException("Only ADMIN users can create accounts.");
        }

        // Verifica si el administrador existe en el repositorio
        if (!adminRepository.existsById(reg.getId())) {
            throw new AdminNotFoundException("Admin with userName " + reg.getUserName() + " not found");
        }

        // Si el administrador existe, guarda y devuelve la cuenta
        return accountRepository.save(account);
    }
}
