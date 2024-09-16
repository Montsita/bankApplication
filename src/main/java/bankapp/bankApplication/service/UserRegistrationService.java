package bankapp.bankApplication.service;


import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.UserRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRegistrationService {
    @Autowired
    UserRegistrationRepository userRegistrationRepository;

    public List<UserRegistration> getAll() { return userRegistrationRepository.findAll(); }

    public Optional<UserRegistration> getById(Long id){ return userRegistrationRepository.findById(id);}

    public Optional<UserRegistration> getRegistrationByUserName(String userName){
        return userRegistrationRepository.findByUserName(userName);
    }

}
