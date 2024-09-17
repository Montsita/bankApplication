package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.AccountStatus;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.interfaces.AccountInterface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Account implements AccountInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType type;
    private String accountNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "balance_currency"))
    })
    private Money balance;

    @JsonIgnore
    private String secretKey;

    @ManyToOne
    private AccountHolder mainOwner;

    //hola

    @ManyToOne
    private AccountHolder secondaryOwner;

    private LocalDate creationDate;
    private LocalTime creationTime;
    private LocalDate lastDateTransaction;
    private LocalTime lastTimeTransaction;
    private LocalDate lastDateUpdatedInterest;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "penaltyFee_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "penaltyFee_currency"))
    })
    private Money penaltyFee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimumBalance_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "minimumBalance_currency"))
    })
    private Money minimumBalance;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthlyMaintenanceFee_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "monthlyMaintenanceFee_currency"))
    })
    private Money monthlyMaintenanceFee;

    //atributs de la credit card, recordar modificar el setter de la creditCard perque no te tases igual que la de estudiants
    // el setMonthlyMaintenanceFee i el minimumBalance

    private Float interestRate; // en caso creditcard Jarko

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "creditLimit_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "creditLimit_currency"))
    })
    private Money creditLimit;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public void interestRateApply(){
        BigDecimal i =new BigDecimal(this.interestRate);
        BigDecimal b = this.getCreditLimit().decreaseAmount(this.balance.getAmount());
        BigDecimal r=b.multiply(i);
        LocalDate date =null;
        switch (this.type){
            case CREDITCARD:
                date =this.lastDateUpdatedInterest.plusDays(30);
                if (date.isEqual(LocalDate.now()) || date.isBefore(LocalDate.now())){
                    r=new BigDecimal(0).subtract(r);
                    interestRateApply(r);
                }
                break;
            case SAVINGS:
                date =this.lastDateUpdatedInterest.plusDays(365);
                if (date.isEqual(LocalDate.now()) || date.isBefore(LocalDate.now())){
                        interestRateApply(r);
                }
                break;
             default:
        }

    }
    private void interestRateApply(BigDecimal interesting){
        if (interesting.compareTo(BigDecimal.ZERO)!=0) {
            Transaction transaction=createTransaction(new Money(interesting));
            transaction.setDescription("InterestRate apply");
            addTransaction(transaction);

            this.getBalance().increaseAmount(interesting);
        }
        this.setLastDateUpdatedInterest(LocalDate.now());
    }
    public Transaction createTransaction(Money amount){
        Transaction transaction= new Transaction();
        transaction.setTransacionDate(LocalDate.now());
        transaction.setTransacionTime(LocalTime.now());
        transaction.setAmount(amount);
        transaction.setBalance(new Money(this.balance.getAmount()));
        transaction.setAccount(this);
        this.balance.increaseAmount(amount.getAmount());
        this.lastDateTransaction=transaction.getTransacionDate();
        this.lastTimeTransaction=transaction.getTransacionTime();
        return transaction;
    }
    public Transaction minimumBalanceControl(){
        if (this.balance.getAmount().compareTo(this.minimumBalance.getAmount())<0) {
            Money amount = new Money(new BigDecimal(0).subtract(this.penaltyFee.getAmount()));
            Transaction transaction = createTransaction(amount);
            transaction.setDescription("PenaltyFee");
            addTransaction(transaction);
            return transaction;
        }else{
            return null;
        }
    }
    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        switch (this.type){
            case STUDENTCHECKING:
                this.monthlyMaintenanceFee.decreaseAmount(monthlyMaintenanceFee.getAmount());
                break;

            case CREDITCARD:
                this.monthlyMaintenanceFee.decreaseAmount(monthlyMaintenanceFee.getAmount());
                break;

            case SAVINGS:
                this.monthlyMaintenanceFee.decreaseAmount(monthlyMaintenanceFee.getAmount());
                break;

            case CHECKING:
                this.monthlyMaintenanceFee.decreaseAmount(monthlyMaintenanceFee.getAmount());
                BigDecimal valor = new BigDecimal("12");
                if (monthlyMaintenanceFee.getAmount().compareTo(valor)!=0)  {
                    this.monthlyMaintenanceFee.increaseAmount(valor);
                }
                break;
            default:

        }

    }
    public void setMinimumBalance(Money minimumBalance) {
        switch (this.type){
            case STUDENTCHECKING:
                this.minimumBalance.decreaseAmount(this.minimumBalance.getAmount());
                break;
            case CREDITCARD:
                this.minimumBalance.decreaseAmount(this.minimumBalance.getAmount());
                break;
            case SAVINGS:
                this.minimumBalance.decreaseAmount(this.minimumBalance.getAmount());
                BigDecimal minor=new BigDecimal("100");
                BigDecimal major=new BigDecimal("1000");
                if (minimumBalance.getAmount().compareTo(minor)<=0 && minimumBalance.getAmount().compareTo(major)>=0) {
                    this.minimumBalance.increaseAmount( minimumBalance.getAmount());
                }else{
                    this.minimumBalance.increaseAmount(minor);
                }
                break;
            case CHECKING:
                this.minimumBalance.decreaseAmount(this.minimumBalance.getAmount());
                BigDecimal valor=new BigDecimal("250");
                if (minimumBalance.getAmount().compareTo(valor)>=0) {
                    this.minimumBalance.increaseAmount(minimumBalance.getAmount());
                }else{
                    this.minimumBalance.increaseAmount(valor);
                }
                break;
            default:

        }
    }

    public void setInterestRate(Float interestRate) {
        switch (this.type){
            case STUDENTCHECKING:
                this.interestRate=0F;
                break;
            case CREDITCARD:
                if (interestRate>=0.1F) {
                    this.interestRate=interestRate;
                }else{
                    this.interestRate=0.1F;
                }
                break;
            case SAVINGS:
                if (interestRate>=0.0025F && interestRate<=0.5F) {
                    this.interestRate=interestRate;
                }else{
                    this.interestRate=0.0025F;
                }
                break;
            case CHECKING:
                this.interestRate=0.0F;
                break;
            default:

        }
    }


    public void setCreditLimit(Money creditLimit) {
        switch (this.type){
            case STUDENTCHECKING:
                this.creditLimit.decreaseAmount(this.creditLimit.getAmount());
                break;
            case CREDITCARD:
                BigDecimal minor=new BigDecimal("100");
                BigDecimal major=new BigDecimal("100000");
                if (creditLimit.getAmount().compareTo(minor)>=0 && creditLimit.getAmount().compareTo(major)<=0) {
                    this.creditLimit.increaseAmount(creditLimit.getAmount());
                }else{
                    this.creditLimit.increaseAmount(minor);
                }
                break;
            case SAVINGS:
                this.creditLimit.decreaseAmount(this.creditLimit.getAmount());
                break;
            case CHECKING:
                this.creditLimit.decreaseAmount(this.creditLimit.getAmount());
                break;
            default:
        }
    }

    public void setType(AccountType type) {
        if(this.type != null) return;
        // Inicializamos la cuenta a los valores por defecto
        this.type = type;

        BigDecimal b1 =BigDecimal.ZERO;
        this.creditLimit = new Money(b1);
        this.minimumBalance = new Money(b1);
        this.monthlyMaintenanceFee = new Money(b1);
        this.accountStatus=AccountStatus.ACTIVE;

        BigDecimal b2 = new BigDecimal("40");
        this.penaltyFee = new Money(b2);
        this.setCreationDate(LocalDate.now());
        this.setLastDateUpdatedInterest(this.getCreationDate());
        setCreditLimit(this.creditLimit);
        setMinimumBalance(this.minimumBalance);
        setMonthlyMaintenanceFee(this.monthlyMaintenanceFee);
        setInterestRate(0F);

    }


    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions == null){
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transaction);
    }
}
