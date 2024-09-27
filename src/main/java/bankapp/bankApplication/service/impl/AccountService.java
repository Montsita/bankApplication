package bankapp.bankApplication.service.impl;
import bankapp.bankApplication.dto.AccountPasswordUpdateDTO;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.InterestType;
import bankapp.bankApplication.enums.TransactionType;
import bankapp.bankApplication.exception.PasswordNotAvailable;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.accounts.Account;
import bankapp.bankApplication.tools.Money;
import bankapp.bankApplication.model.transactions.Transaction;
import bankapp.bankApplication.repository.impl.AccountRepository;
import bankapp.bankApplication.repository.impl.AdminRepository;
import bankapp.bankApplication.repository.impl.TransactionRepository;

import bankapp.bankApplication.tools.Operation;
import bankapp.bankApplication.tools.ResInterestCalculation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdminRepository adminRepository;


    @Autowired(required = true)
    private UserRegistrationService userRegistrationService;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> getById(Long id) {
        Optional<Account> account= accountRepository.findById(id);
        if (account.isPresent()){
            Account acc=account.get();
            this. calculateInterstRate(acc);
        }
        return account;
    }

    public Account create(Account account) throws UnauthorizedException {
        return accountRepository.save(account);
    }

    public Optional<Account> change(Account account) throws UnauthorizedException {
        if (accountRepository.existsById(account.getId())) {
            return Optional.of(accountRepository.save(account));
        }
        return Optional.empty();
    }

    public boolean delete(Long id) throws UnauthorizedException {
        if(accountRepository.existsById(id)){
            accountRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public Transaction createMovement (Long id, BigDecimal amount ){
        if (accountRepository.existsById(id)){
            Account account=accountRepository.getById(id);

            if (amount.compareTo(BigDecimal.ZERO)<0){
                if (account.getBalance().getAmount().compareTo(amount.abs()) < 0){
                    return null;
                }
            }

            Money money=new Money(amount);
            Transaction transaction=account.createTransaction(money);
            if (amount.compareTo(BigDecimal.ZERO)>0) {
                transaction.setDescription(TransactionType.DEPOSIT);
            }else if(amount.compareTo(BigDecimal.ZERO)<0){
                transaction.setDescription(TransactionType.WITHDRAW);
            }
            transactionRepository.save(transaction);
            account.addTransaction(transaction);

            Transaction transactionPenalty=account.minimumBalanceControl();
            if (transactionPenalty !=null) {
                account.addTransaction(transactionPenalty);
                transactionRepository.save(transactionPenalty);
            }

            accountRepository.save(account);
            return transaction;
        }

        return null;
    }

    public Transaction createTransfer(Long id, BigDecimal amount, Long destinyId, String concept){
        if (amount.compareTo(BigDecimal.ZERO)>0){
            if (!id.equals(destinyId)) {

                if (accountRepository.existsById(id) && accountRepository.existsById(destinyId )){
                    Account accountOrigin=accountRepository.getById(id);
                    if (accountOrigin.getBalance().getAmount().compareTo(amount)<0){
                        return null;
                    }

                    BigDecimal subtraction=new BigDecimal(0).subtract(amount);//negativo
                    Money moneyOrigin=new Money(subtraction);
                    Transaction transactionOrigin =accountOrigin.createTransaction(moneyOrigin);
                    transactionOrigin.setDescription(TransactionType.TRANSFER);
                    transactionOrigin.setOriginId(id);
                    transactionOrigin.setDestinyId(destinyId);
                    transactionOrigin.setConcept(concept);
                    transactionRepository.save(transactionOrigin);
                    accountOrigin.addTransaction(transactionOrigin);

                    Transaction transactionPenalty=accountOrigin.minimumBalanceControl();
                    if (transactionPenalty !=null) {
                        accountOrigin.addTransaction(transactionPenalty);
                        transactionRepository.save(transactionPenalty);
                    }
                    accountRepository.save(accountOrigin);

                    Account accountDestiny=accountRepository.getById(destinyId);
                    Money moneyDestiny=new Money(amount);
                    Transaction transactionDestiny =accountDestiny.createTransaction(moneyDestiny);
                    transactionDestiny.setDescription(TransactionType.TRANSFER);
                    transactionDestiny.setOriginId(id);
                    transactionDestiny.setDestinyId(destinyId);
                    transactionDestiny.setConcept(concept);
                    transactionRepository.save(transactionDestiny);
                    accountOrigin.addTransaction(transactionDestiny);
                    accountRepository.save(accountDestiny);

                    return transactionOrigin;

                }
            }

        }
        return null;
    }

    public boolean changePassword(Long id , AccountPasswordUpdateDTO accountPasswordUpdateDTO) throws PasswordNotAvailable {
        if (accountRepository.existsById(id)) {
            Account account =accountRepository.getById(id);
            if (account.getSecretKey().equals((accountPasswordUpdateDTO.getOldPassword()))) {
                account.setSecretKey(accountPasswordUpdateDTO.getNewPassword());
                accountRepository.save(account);
                return true;
            }else{
                throw new PasswordNotAvailable("The password is not available");
            }
        }
        return false;
    }

    public Boolean isAccountFromUserId(Long accountId, Long userId) {
        if (accountRepository.existsById(accountId)){
            Account account=accountRepository.getById(accountId);
            boolean b1 = account.getMainOwner().getId().equals(userId) ;
            boolean b2 = account.getSecondaryOwner().getId().equals(userId);
            return b1 || b2;
        }
        return false;
    }

    public void calculateInterstRate(Account account){
        ResInterestCalculation res=null;
        if(account.getType()== AccountType.CREDITCARD) {
            BigDecimal interestCreditCard=BigDecimal.ZERO.subtract(account.getInterestRate());
            BigDecimal amount=account.getCreditLimit().getAmount().subtract(account.getBalance().getAmount());
            res= Operation.interestCalculation(account.getNextDateUpdateInterest(),
                    LocalDate.now(),amount,interestCreditCard, InterestType.DIARY);
        }else{
            res= Operation.interestCalculation(account.getNextDateUpdateInterest(),
                    LocalDate.now(),account.getBalance().getAmount(),account.getInterestRate(), InterestType.DIARY);
        }

        if (res!=null && res.getCalculation().compareTo(BigDecimal.ZERO)!=0){
            Transaction transaction=account.createTransaction(new Money(res.getCalculation()));
            transaction.setDescription(TransactionType.INTERESTRATE);
            transactionRepository.save(transaction);
            account.addTransaction(transaction);
            account.setNextDateUpdateInterest(res.getNextDateCalculation());
            accountRepository.save(account);

            //penalty transaction
            Transaction transactionPenalty=account.minimumBalanceControl();
            if (transactionPenalty !=null){
                account.addTransaction(transactionPenalty);
                transactionRepository.save(transactionPenalty);
            }
            accountRepository.save(account);
        }
    }
}