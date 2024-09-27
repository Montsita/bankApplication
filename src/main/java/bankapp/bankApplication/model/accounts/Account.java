package bankapp.bankApplication.model.accounts;

import bankapp.bankApplication.enums.AccountStatus;
import bankapp.bankApplication.enums.AccountType;
import bankapp.bankApplication.enums.TransactionType;
import bankapp.bankApplication.interfaces.AccountInterface;
import bankapp.bankApplication.model.transactions.Transaction;
import bankapp.bankApplication.model.users.AccountHolder;
import bankapp.bankApplication.tools.Money;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @ManyToOne
    private AccountHolder secondaryOwner;

    private LocalDate creationDate;
    private LocalTime creationTime;
    private LocalDate lastDateUpdatedInterest;
    private LocalTime lastTimeUpdatedInterest;
    private LocalDate nextDateUpdateInterest;
    //changes

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "penaltyFee_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "penaltyFee_currency"))
    })
    private Money penaltyFee;
    private Boolean isPenalitzed;
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

    @Column(precision = 20 , scale = 17)
    private BigDecimal interestRate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "creditLimit_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "creditLimit_currency"))
    })
    private Money creditLimit;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;


    public Transaction createTransaction(Money amount){
        Transaction transaction= new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionTime(LocalTime.now());
        transaction.setAmount(amount);
        transaction.setBalance(new Money(this.balance.getAmount()));
        transaction.setAccount(this);
        this.balance.increaseAmount(amount.getAmount());
        return transaction;
    }
    public Transaction minimumBalanceControl(){
        if (this.balance.getAmount().compareTo(this.minimumBalance.getAmount())<0) {
            if(this.isPenalitzed==false) {
                Money amount = new Money(new BigDecimal(0).subtract(this.penaltyFee.getAmount()));
                Transaction transaction = createTransaction(amount);
                transaction.setDescription(TransactionType.PENALTYFEE);
                // addTransaction(transaction);
                this.isPenalitzed=true;
                return transaction;
            }
        }else{
            if(this.isPenalitzed==true) {
                this.isPenalitzed=false;
            }
        }
        return null;
    }
    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        switch (this.type){
            case CHECKING:
                BigDecimal valor = new BigDecimal("12");
                if (monthlyMaintenanceFee.getAmount().compareTo(valor)>0)  {
                    this.monthlyMaintenanceFee=monthlyMaintenanceFee;
                }else {
                    this.monthlyMaintenanceFee=new Money(valor);
                }
                break;
        }

    }
    public void setMinimumBalance(Money minimumBalance) {
        switch (this.type){
            case SAVINGS:
                BigDecimal min=new BigDecimal("100");
                BigDecimal max=new BigDecimal("1000");
                if (minimumBalance.getAmount().compareTo(min)<0){
                    this.minimumBalance= new Money(min);
                } else if (minimumBalance.getAmount().compareTo(max)>0){
                    this.minimumBalance = new Money(max);
                }else{
                    this.minimumBalance=minimumBalance;
                }
                break;

            case CHECKING:
                BigDecimal valor=new BigDecimal("250");
                if (minimumBalance.getAmount().compareTo(valor)>=0) {
                    this.minimumBalance=minimumBalance;
                } else if (minimumBalance.getAmount().compareTo(valor)<=0){
                    this.minimumBalance=new Money(valor);
                }
                break;
            default:

        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        switch (this.type){
            case CREDITCARD:
                BigDecimal valueCC=new BigDecimal("0.1");
                if (interestRate == null){
                    this.interestRate=valueCC;
                }else if (interestRate.compareTo(valueCC)<0) {
                    this.interestRate=valueCC;
                }else {
                    this.interestRate=interestRate;
                }
                break;

            case SAVINGS:
                BigDecimal min=new BigDecimal("0.0025");
                BigDecimal max=new BigDecimal("0.5");
                if (interestRate.compareTo(min)<0){
                    this.interestRate= min;
                } else if (interestRate.compareTo(max)>0){
                    this.interestRate = max;
                }else{
                    this.interestRate=interestRate;
                }
                break;
            default:
        }
    }


    public void setCreditLimit(Money creditLimit) {
        switch (this.type){
            case CREDITCARD:
                BigDecimal minor=new BigDecimal("100");
                BigDecimal major=new BigDecimal("100000");
                if (creditLimit.getAmount().compareTo(minor)<0){
                    this.creditLimit= new Money(minor);
                } else if (creditLimit.getAmount().compareTo(major)>0){
                    this.creditLimit = new Money(major);
                }else{
                    this.creditLimit=creditLimit;
                }

                break;
            default:
        }
    }

    public void setType(AccountType type) {
        if(this.type != null) return;
        this.type = type;

        this.accountStatus=AccountStatus.ACTIVE;
        this.creationDate=LocalDate.now();
        this.creationTime=LocalTime.now();
        this.lastDateUpdatedInterest=this.getCreationDate();
        this.penaltyFee = new Money(new BigDecimal("40"));
        this.isPenalitzed=false;
        this.setSecretKey("auto");
        initializeDefaultValue(this.type);
    }
    private void initializeDefaultValue(AccountType accountType){
        switch (this.type){
            case CREDITCARD:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("0"));
                this.minimumBalance= new Money(new BigDecimal("0"));
                this.interestRate=new BigDecimal("0.1");
                this.creditLimit=new Money(new BigDecimal("100"));
                this.nextDateUpdateInterest=this.getCreationDate();
                this.balance=this.creditLimit;
                break;
            case SAVINGS:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("0"));
                this.minimumBalance=new Money(new BigDecimal("100"));
                this.interestRate=new BigDecimal("0.0025");
                this.creditLimit=new Money(new BigDecimal("0"));
                this.nextDateUpdateInterest=this.getCreationDate();
                break;
            case CHECKING:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("12"));
                this.minimumBalance=new Money(new BigDecimal("250"));
                this.interestRate=new BigDecimal("0");
                this.creditLimit=new Money(new BigDecimal("0"));
                this.nextDateUpdateInterest=this.getCreationDate();
                break;
            case STUDENTCHECKING:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("0"));
                this.minimumBalance=new Money(new BigDecimal("0"));
                this.interestRate=new BigDecimal("0");
                this.creditLimit=new Money(new BigDecimal("0"));
                this.nextDateUpdateInterest=this.getCreationDate();
                break;
            default:

        }

    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (this.transactions == null){
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transaction);
    }

}
