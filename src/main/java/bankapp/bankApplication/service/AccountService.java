package bankapp.bankApplication.service;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.exception.AdminNotFoundException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Admin;
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

    public List<Account> getAll() { return accountRepository.findAll(); }

    public Optional<Account> getById(Long id){ return accountRepository.findById(id); }

    public Account create(Account account, Admin admin) {
        // Verifica si el admin existe en el repositorio
        if (!adminRepository.existsById(admin.getId())) {
            throw new AdminNotFoundException("Admin with ID " + admin.getId() + " not found");
        }

        // Si el administrador existe, guarda y devuelve la cuenta
        return accountRepository.save(account);
    }
}
