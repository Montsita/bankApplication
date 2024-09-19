package bankapp.bankApplication.service;

import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Address;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.repository.AccountHolderRepository;
import bankapp.bankApplication.repository.AdminRepository;
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

    public Admin create(Admin admin , String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return adminRepository.save(admin);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create admin.");
        }
    }
    public Optional<Admin> change(Admin admin, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (adminRepository.existsById(admin.getId())) {
                return Optional.of(adminRepository.save(admin));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change admin.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(adminRepository.existsById(id)){
                adminRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete admin.");
        }
    }
}
