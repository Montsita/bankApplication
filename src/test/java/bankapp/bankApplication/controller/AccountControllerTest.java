package bankapp.bankApplication.controller;

import bankapp.bankApplication.controller.impl.AccountController;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.accounts.Account;
import bankapp.bankApplication.model.users.AccountHolder;
import bankapp.bankApplication.model.users.Admin;
import bankapp.bankApplication.model.contacts.Address;
import bankapp.bankApplication.model.registrations.UserRegistration;
import bankapp.bankApplication.repository.impl.*;
import bankapp.bankApplication.service.impl.AccountService;
import bankapp.bankApplication.tools.Money;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testng.AssertJUnit.assertEquals;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountHolderRepository accountHolderRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private UserRegistrationRepository userRegistrationRepository;



    @Autowired
    private ObjectMapper objectMapper;

    //private Account , argentina;


    private Money bg;
    private Account accountCreditCard;
    private Account accountChecking;
    private Account accountSavings;

    @BeforeEach
    public void setUp() {
        bg = new Money(new BigDecimal(2000));

        Address adress1 = new Address();
        adress1.setCity("Madrid");
        adress1.setCountry("Spain");
        adress1.setDistrict("Center");
        adress1.setEntrancePortal("5");
        adress1.setPostalCode("28013");
        adress1.setStreet("Calle Mayor");
        addressRepository.save(adress1);

        AccountHolder accountHolder1 = new AccountHolder();
        accountHolder1.setName("Antonia");
        accountHolder1.setDataOfBirth(LocalDate.parse("1980-12-21"));
        accountHolder1.setMailingAddress("antonia@gmail.com");
        accountHolder1.setPrimaryAddress(adress1);
        accountHolderRepository.save(accountHolder1);


        accountChecking = new Account();
        accountChecking.setType(AccountType.CHECKING);
        accountChecking.setMainOwner(accountHolder1);
        accountChecking.setBalance(bg);
        accountChecking.setCreationDate(LocalDate.now());
        accountRepository.save(accountChecking);

        accountCreditCard = new Account();
        accountCreditCard.setType(AccountType.CREDITCARD);
        accountCreditCard.setMainOwner(accountHolder1);
        accountCreditCard.setBalance(bg);
        accountCreditCard.setCreationDate(LocalDate.now());
        accountRepository.save(accountCreditCard);

        accountSavings = new Account();
        accountSavings.setType(AccountType.SAVINGS);
        accountSavings.setMainOwner(accountHolder1);
        accountSavings.setBalance(bg);
        accountSavings.setCreationDate(LocalDate.now());
        accountRepository.save(accountSavings);

        Admin admin1= new Admin() ;
        admin1.setName("Montse");
        adminRepository.save(admin1);

        UserRegistration userRegistration1 = new UserRegistration(admin1);
        userRegistration1.setUsername("Montsita");
        userRegistrationRepository.save(userRegistration1);

        admin1.setUserRegistration(userRegistration1);
        adminRepository.save(admin1);
    }

    @Test
    public void testGetAllAccounts() throws Exception{
        List<Account> accounts = Arrays.asList(accountChecking, accountCreditCard);

        when(accountService.getAll()).thenReturn(accounts);

        mockMvc.perform(get("/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    public void testGetAccountById() throws Exception{
        Long accountId = 1L;

        when(accountService.getById(accountId)).thenReturn(Optional.of(accountChecking));

        mockMvc.perform(get("/account/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainOwner.name").value("Antonia"))
                .andExpect(jsonPath("$.type").value("CHECKING"));
    }

    @Test
    public void testDeleteAccounts() throws Exception{
        Long accountId = 1L;

        when(accountService.delete(accountId)).thenReturn(true);

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNoAdmin() throws Exception{
        Long accountId = 1L;

        when(accountService.delete(accountId))
                .thenThrow(new UnauthorizedException("Only ADMIN users can delete accounts."));

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Only ADMIN users can delete accounts."));
    }

    @Test
    public void testDeleteAdmin() throws Exception{
        Long accountId = 1L;

        when(accountService.delete(accountId)).thenReturn(true);

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSetCreditLimitForCreditCard() {

        Money validLimit = new Money(new BigDecimal("5000"));
        accountCreditCard.setCreditLimit(validLimit);
        assertEquals(validLimit.getAmount(), accountCreditCard.getCreditLimit().getAmount());


        Money invalidLimitLow = new Money(new BigDecimal("50"));
        accountCreditCard.setCreditLimit(invalidLimitLow);
        assertEquals(new BigDecimal("100.00"), accountCreditCard.getCreditLimit().getAmount());  // valor mínimo permitido


        Money invalidLimitHigh = new Money(new BigDecimal("150000"));
        accountCreditCard.setCreditLimit(invalidLimitHigh);
        assertEquals(new BigDecimal("100000.00"), accountCreditCard.getCreditLimit().getAmount());  // valor mínimo por defecto
    }

    @Test
    public void testSetInterestRateForCreditCard() {

        Money validLimit = new Money(new BigDecimal("0.15"));
        accountCreditCard.setInterestRate(validLimit.getAmount());
        assertEquals(new BigDecimal("0.15"), accountCreditCard.getInterestRate());

        Money invalidLimitLow = new Money(new BigDecimal("0.05"));
        accountCreditCard.setInterestRate(invalidLimitLow.getAmount());
        assertEquals(new BigDecimal("0.1"), accountCreditCard.getInterestRate());

        accountCreditCard.setInterestRate(null);
        assertEquals(new BigDecimal("0.1"), accountCreditCard.getInterestRate());  // Valor por defecto
    }

    @Test
    public void testSetMonthlyMaintenanceFeeChecking() {

        Money validLimit = new Money(new BigDecimal(15));
        accountChecking.setMonthlyMaintenanceFee(validLimit);
        assertEquals(validLimit.getAmount(), accountChecking.getMonthlyMaintenanceFee().getAmount());


        Money invalidLimitLow = new Money(new BigDecimal(10));
        accountChecking.setMonthlyMaintenanceFee(invalidLimitLow);
        assertEquals(new BigDecimal("12.00"), accountChecking.getMonthlyMaintenanceFee().getAmount());
    }

    @Test
    public void testSetMinimumBalanceChecking() {

        Money validLimit = new Money(new BigDecimal(500));
        accountChecking.setMinimumBalance(validLimit);
        assertEquals(validLimit.getAmount(), accountChecking.getMinimumBalance().getAmount());


        Money invalidLimitLow = new Money(new BigDecimal(10));
        accountChecking.setMinimumBalance(invalidLimitLow);
        assertEquals(new BigDecimal("250.00"), accountChecking.getMinimumBalance().getAmount());
    }

    @Test
    public void testSetMinimumBalanceSavings() {

        Money validLimit = new Money(new BigDecimal(500));
        accountSavings.setMinimumBalance(validLimit);
        assertEquals(validLimit.getAmount(), accountSavings.getMinimumBalance().getAmount());


        Money invalidLimitLow = new Money(new BigDecimal(10));
        accountSavings.setMinimumBalance(invalidLimitLow);
        assertEquals(new BigDecimal("100.00"), accountSavings.getMinimumBalance().getAmount());
    }

    @Test
    public void testSetInterestRateSavings() {

        BigDecimal validLimit = new BigDecimal(0.4);
        accountSavings.setInterestRate(validLimit);
        assertEquals(validLimit, accountSavings.getInterestRate());


        BigDecimal invalidLimitLow = new BigDecimal(0.001);
        accountSavings.setInterestRate(invalidLimitLow);
        assertEquals(new BigDecimal("0.0025"), accountSavings.getInterestRate());

        BigDecimal invalidLimitMax = new BigDecimal(1);
        accountSavings.setInterestRate(invalidLimitMax);
        assertEquals(new BigDecimal("0.5"), accountSavings.getInterestRate());
    }
}
