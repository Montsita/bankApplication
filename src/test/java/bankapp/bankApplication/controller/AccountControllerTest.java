package bankapp.bankApplication.controller;

import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.UserType;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.*;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Admin;
import bankapp.bankApplication.repository.*;
import bankapp.bankApplication.service.AccountService;
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
    private Money pf;
    private Account account1;
    private Account account2;

    @BeforeEach
    public void setUp() {
        bg = new Money(new BigDecimal(2000));
        pf = new Money(new BigDecimal(40));

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


        account1 = new Account();
        account1.setType(AccountType.CHECKING);
        account1.setMainOwner(accountHolder1);
        account1.setBalance(bg);
        account1.setCreationDate(LocalDate.now());
        //accountRepository.save(account1);

        account2 = new Account();
        account2.setType(AccountType.CREDITCARD);
        account2.setMainOwner(accountHolder1);
        account2.setBalance(bg);
        account2.setCreationDate(LocalDate.now());
        accountRepository.save(account2);

        Admin admin1= new Admin() ;
        admin1.setName("Montse");
        adminRepository.save(admin1);

        UserRegistration userRegistration1 = new UserRegistration(admin1);
        userRegistration1.setType(UserType.ADMIN);
        userRegistration1.setUserName("Montsita");
        userRegistrationRepository.save(userRegistration1);

        admin1.setUserRegistration(userRegistration1);
        adminRepository.save(admin1);
    }

    @Test
    public void testGetAllAccounts() throws Exception{
        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountService.getAll()).thenReturn(accounts);

        mockMvc.perform(get("/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    public void testGetAccountById() throws Exception{
        Long accountId = 1L;

        when(accountService.getById(accountId)).thenReturn(Optional.of(account1));

        mockMvc.perform(get("/account/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainOwner.name").value("Antonia"))
                .andExpect(jsonPath("$.type").value("CHECKING"));
    }

    @Test
    public void testDeleteAccounts() throws Exception{
        Long accountId = 1L;
        String userName = "Montsita";

        when(accountService.delete(accountId, userName)).thenReturn(true);

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .param("userName", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNoAdmin() throws Exception{
        Long accountId = 1L;
        String userName = "Antonia";

        when(accountService.delete(accountId, userName))
                .thenThrow(new UnauthorizedException("Only ADMIN users can delete accounts."));

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .param("userName", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Only ADMIN users can delete accounts."));
    }

    @Test
    public void testDeleteAdmin() throws Exception{
        Long accountId = 1L;
        String userName = "Montsita";

        when(accountService.delete(accountId, userName)).thenReturn(true);

        mockMvc.perform(delete("/account/delete/{id}", accountId)
                        .param("userName", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    //verificar que solo el admin puede borrar, crear y modificar
    //admin
    //valors predeterminats

    @Test
    public void testSetCreditLimitForCreditCard() {

        Money validLimit = new Money(new BigDecimal("5000"));
        account2.setCreditLimit(validLimit);
        assertEquals(validLimit.getAmount(), account2.getCreditLimit().getAmount());


        Money invalidLimitLow = new Money(new BigDecimal("50"));
        account2.setCreditLimit(invalidLimitLow);
        assertEquals(new BigDecimal("100.00"), account2.getCreditLimit().getAmount());  // valor mínimo permitido


        Money invalidLimitHigh = new Money(new BigDecimal("150000"));
        account2.setCreditLimit(invalidLimitHigh);
        assertEquals(new BigDecimal("100000.00"), account2.getCreditLimit().getAmount());  // valor mínimo por defecto
    }

    @Test
    public void testSetInterestRateForCreditCard() {

        // Caso con tasa de interés válida
        Money validLimit = new Money(new BigDecimal("0.15"));
        account2.setInterestRate(validLimit.getAmount());
        assertEquals(new BigDecimal("0.15"), account2.getInterestRate());

        // Caso con tasa de interés inferior al mínimo
        Money invalidLimitLow = new Money(new BigDecimal("0.05"));
        account2.setInterestRate(invalidLimitLow.getAmount());
        assertEquals(new BigDecimal("0.1"), account2.getInterestRate());

        // Caso con tasa de interés nula o no válida
        account2.setInterestRate(null);
        assertEquals(new BigDecimal("0.1"), account2.getInterestRate());  // Valor por defecto
    }

    //VOY POR AQUI

    @Test
    public void testSetMonthlyMaintenanceFee() {
        account1.setType(AccountType.CHECKING);

        Money validLimit = new Money(new BigDecimal(15));
        account1.setMonthlyMaintenanceFee(validLimit);
        assertEquals(validLimit.getAmount(), account1.getMonthlyMaintenanceFee().getAmount());


        Money invalidLimit = new Money(new BigDecimal(15));
        account1.setMonthlyMaintenanceFee(validLimit);
        assertEquals(validLimit.getAmount(), account1.getMonthlyMaintenanceFee().getAmount());
    }
}
