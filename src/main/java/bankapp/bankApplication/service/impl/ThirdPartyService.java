package bankapp.bankApplication.service.impl;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.ThirdParty;
import bankapp.bankApplication.model.registrations.UserRegistration;
import bankapp.bankApplication.repository.impl.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThirdPartyService {

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    public List<ThirdParty> getAll() { return thirdPartyRepository.findAll(); }

    public Optional<ThirdParty> getById(Long id){ return thirdPartyRepository.findById(id); }

    public ThirdParty create(ThirdParty thirdParty, String userName) throws UnauthorizedException {
        UserRegistration userRegistration =new UserRegistration();
        userRegistration.setUsername(userName);
        userRegistration=userRegistrationService.create(userRegistration);
        thirdParty.setUserRegistration(userRegistration);
        return thirdPartyRepository.save(thirdParty);
    }

    public Optional<ThirdParty> change(ThirdParty thirdParty ) throws UnauthorizedException {
        if (thirdPartyRepository.existsById(thirdParty.getId())) {
            return Optional.of(thirdPartyRepository.save(thirdParty));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(thirdPartyRepository.existsById(id)){
            thirdPartyRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}