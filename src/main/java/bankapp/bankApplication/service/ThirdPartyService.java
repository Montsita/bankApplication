package bankapp.bankApplication.service;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.model.ThirdParty;
import bankapp.bankApplication.repository.AdminRepository;
import bankapp.bankApplication.repository.ThirdPartyRepository;
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

    public ThirdParty create(ThirdParty thirdParty , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return thirdPartyRepository.save(thirdParty);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create thirdParty.");
        }
    }
    public Optional<ThirdParty> change(ThirdParty thirdParty , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (thirdPartyRepository.existsById(thirdParty.getId())) {
                return Optional.of(thirdPartyRepository.save(thirdParty));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change thirdParty.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(thirdPartyRepository.existsById(id)){
                thirdPartyRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete thirdParty.");
        }
    }
}