package bankapp.bankApplication.service;


import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.User;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.UserRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    public boolean isAdmin(String userName)  {
        Optional<UserRegistration> regOptional = getRegistrationByUserName(userName);
        if (regOptional.isPresent()) {
            if (regOptional.get().getType() == UserType.ADMIN) {
                return true;
            }
        }
        return false;
    }
    public Long idUserByUserName(String userName){
       Optional<UserRegistration>  user = userRegistrationRepository.findByUserName(userName);
       return user.map(userRegistration -> userRegistration.getUser().getId()).orElse(null);
    }
    public String nameCompletByUserName(String userName){
        Optional<UserRegistration>  user = userRegistrationRepository.findByUserName(userName);
        return user.map(userRegistration -> userRegistration.getUser().getName()).orElse(null);
    }
}
