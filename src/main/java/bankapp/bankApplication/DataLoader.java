package bankapp.bankApplication;

import bankapp.bankApplication.model.*;

import bankapp.bankApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader  implements CommandLineRunner {
    @Autowired
    private AddressRepository addressRepository ;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private UserRegistrationRepository userRegistrationRepository;

    @Override
    public void run(String... args) throws Exception {

        // Create new admin and link it with the userRegistration
        Admin admin1= new Admin() ;
        admin1.setName("Montse");
        adminRepository.save(admin1);

        UserRegistration userRegistration1 = new UserRegistration(admin1);
        userRegistrationRepository.save(userRegistration1);

        admin1.setUserRegistration(userRegistration1);
        adminRepository.save(admin1);

    }
}
