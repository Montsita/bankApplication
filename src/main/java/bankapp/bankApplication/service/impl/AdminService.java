package bankapp.bankApplication.service.impl;

import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.users.Admin;
import bankapp.bankApplication.model.registrations.UserRegistration;
import bankapp.bankApplication.repository.impl.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    public List<Admin> getAll() { return adminRepository.findAll(); }

    public Optional<Admin> getById(Long id){
        return adminRepository.findById(id);
    }

    public Admin create(Admin admin) throws UnauthorizedException {
        return adminRepository.save(admin);
    }
    public Optional<Admin> change(Admin admin) throws UnauthorizedException {
        if (adminRepository.existsById(admin.getId())) {
            return Optional.of(adminRepository.save(admin));
        }
        return Optional.empty();
    }

    public Admin create(Admin admin , String userName) throws UnauthorizedException {
        UserRegistration userRegistration =new UserRegistration();
        userRegistration.setUsername(userName);
        userRegistration=userRegistrationService.create(userRegistration);
        admin.setUserRegistration(userRegistration);
        return adminRepository.save(admin);
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(adminRepository.existsById(id)){
            adminRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
