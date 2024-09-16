package bankapp.bankApplication.service;


import bankapp.bankApplication.dto.RegistrationUpdateAllDTO;
import bankapp.bankApplication.model.UserRegistration;
import bankapp.bankApplication.repository.UserRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {
    @Autowired
    UserRegistrationRepository userRegistrationRepository;


    public UserRegistration getRegistrationByUserName(RegistrationUpdateAllDTO userName){
        return userRegistrationRepository.findByUserName(userName.getUserName());
    }
}
