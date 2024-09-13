package bankapp.bankApplication;

import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.model.*;

import bankapp.bankApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        userRegistration1.setType(UserType.ADMIN);
        userRegistration1.setUserName("Montsita");
        userRegistrationRepository.save(userRegistration1);

        admin1.setUserRegistration(userRegistration1);
        adminRepository.save(admin1);

        Admin admin2 = new Admin();
        admin2.setName("Jose");
        adminRepository.save(admin2);

        UserRegistration userRegistration2 = new UserRegistration(admin2);
        userRegistration2.setType(UserType.ADMIN);
        userRegistration2.setUserName("Joselito");
        userRegistrationRepository.save(userRegistration2);

        admin2.setUserRegistration(userRegistration2);
        adminRepository.save(admin2);



        AccountHolder accountHolder1 = new AccountHolder();
        accountHolder1.setName("Antonia");
        accountHolder1.setDataOfBirth(LocalDate.parse("1980-12-21"));
        accountHolder1.setMailingAddress("antonia@gmail.com");
        Address adress1 = new Address();
        adress1.setCity("Madrid");
        adress1.setCountry("Spain");
        adress1.setDistrict("Center");
        adress1.setEntrancePortal("5");
        adress1.setPostalCode("28013");
        adress1.setStreet("Calle Mayor");
        addressRepository.save(adress1);
        accountHolder1.setPrimaryAddress(adress1);
        accountHolderRepository.save(accountHolder1);

        AccountHolder accountHolder2 = new AccountHolder();
        accountHolder2.setName("Manolo");
        accountHolder2.setDataOfBirth(LocalDate.parse("1982-09-11"));
        accountHolder2.setMailingAddress("manolo@gmail.com");
        accountHolder2.setPrimaryAddress(adress1);
        accountHolderRepository.save(accountHolder2);

        Money money = new Money(new BigDecimal(20000));
        Account accountChk1 = new Account();
        accountChk1.setType(AccountType.CHECKING);
        System.out.println("CREDIT LIMIT" +accountChk1.getCreditLimit().toString());
        accountChk1.setBalance(money);
        accountRepository.save(accountChk1);
        System.out.println("CREDIT LIMIT" +accountChk1.getCreditLimit().toString());








    }
}
