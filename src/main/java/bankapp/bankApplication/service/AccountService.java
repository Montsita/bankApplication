package bankapp.bankApplication.service;
import bankapp.bankApplication.dto.AccountPasswordUpdateDTO;
import bankapp.bankApplication.exception.PasswordNotAvailable;
import bankapp.bankApplication.exception.UnauthorizedException;
import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.Money;
import bankapp.bankApplication.model.Transaction;
import bankapp.bankApplication.repository.AccountRepository;
import bankapp.bankApplication.repository.AdminRepository;
import bankapp.bankApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> getById(Long id) {
        Optional<Account> account= accountRepository.findById(id);
        if (account.isPresent()){
            account.get().interestRateApplyAA();
            accountRepository.save(account.get());
        }
        return account;
    }

    public Account create(Account account, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            return accountRepository.save(account);
        }else{
            throw new UnauthorizedException("Only ADMIN users can create accounts.");
        }
    }

    public Optional<Account> change(Account account, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if (accountRepository.existsById(account.getId())) {
                return Optional.of(accountRepository.save(account));
            }
            return Optional.empty();
        }else{
            throw new UnauthorizedException("Only ADMIN users can change accounts.");
        }
    }

    public boolean delete(Long id, String userName) throws UnauthorizedException {
        if (userRegistrationService.isAdmin(userName)) {
            if(accountRepository.existsById(id)){
                accountRepository.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else{
            throw new UnauthorizedException("Only ADMIN users can delete accounts.");
        }
    }

    public Transaction createMovement (Long id, BigDecimal amount, String userName ){
        Long userId=userRegistrationService.idUserByUserName(userName);
        String name=userRegistrationService.nameCompletByUserName(userName);
        boolean isAdmin=userRegistrationService.isAdmin(userName);
        if (userId!=0 && (isAdmin || this.isAccountFromUserId(id,userId))){
            if (accountRepository.existsById(id)){
                Account account=accountRepository.getById(id);

                if (amount.compareTo(BigDecimal.ZERO)<0){
                    if (account.getBalance().getAmount().compareTo(amount.abs()) < 0){
                        return null;
                    }
                }

                Money money=new Money(amount);
                Transaction transaction=account.createTransaction(money);
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
        }
        return null;
    }

    public Transaction createTransfer(Long id, BigDecimal amount, Long destinyId, String concept, String userName){
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
}