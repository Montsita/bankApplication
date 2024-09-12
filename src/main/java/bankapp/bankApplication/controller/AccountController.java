package bankapp.bankApplication.controller;

import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

   // @PostMapping
   // public Account createAccount(@RequestBody Account account){ return accountService.createAccount(account);}
    //IMPORTANTE QUE LO PRIMERO QUE PIDA SEA EL TYPEACCOUNT, para que le de valores iniciales y si el usuario nos da un
    // valor que se sobrescriban con los valores del usuario


}
