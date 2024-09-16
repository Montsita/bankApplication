package bankapp.bankApplication.service;
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

    public List<AccountHolder> getAll() { return accountHolderRepository.findAll(); }

    public Optional<AccountHolder> getById(Long id){ return accountHolderRepository.findById(id); }
}
