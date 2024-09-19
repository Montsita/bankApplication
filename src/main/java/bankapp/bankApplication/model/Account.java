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
import java.math.RoundingMode;
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

    public void interestRateApply(){
        BigDecimal i =this.interestRate;
        BigDecimal b = this.getCreditLimit().decreaseAmount(this.balance.getAmount());
        BigDecimal r=b.multiply(i);
        switch (this.type){
            case CREDITCARD:
                LocalDate dateCard =this.lastDateUpdatedInterest.plusDays(30);
                if (dateCard.isEqual(LocalDate.now()) || dateCard.isBefore(LocalDate.now())){
                    r=new BigDecimal(0).subtract(r);
                    interestRateApply(r);
                }
                break;
            case SAVINGS:
                LocalDate dateSavings=this.lastDateUpdatedInterest.plusDays(365);
                if (dateSavings.isEqual(LocalDate.now()) || dateSavings.isBefore(LocalDate.now())){
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

    public void interestRateApplyAA(){
        LocalDate today=LocalDate.now();
        if (today.isEqual(this.nextDateUpdateInterest) || today.isAfter(this.nextDateUpdateInterest) ){
            BigDecimal monthlyRate = this.getInterestRate().divide(new BigDecimal("12"),6, RoundingMode.DOWN);
            BigDecimal base = null;
            int nextDays=0;
            switch (this.type){
                case CREDITCARD:
                    monthlyRate=monthlyRate.multiply(new BigDecimal(-1));
                    base=this.creditLimit.getAmount();
                    base=base.subtract(this.balance.getAmount());
                    nextDays=30;
                    if (base.compareTo(BigDecimal.ZERO)==0){
                        return;
                    }
                    break;
                case SAVINGS:
                    base=this.balance.getAmount();
                    nextDays=365;
                    break;
                default:
                    return;
            }
            BigDecimal interest = base.multiply(monthlyRate).setScale(2, RoundingMode.DOWN);
            this.balance.increaseAmount(interest);
            System.out.println("interes: " + interest   + " %=" + monthlyRate + " monto=" + balance.getAmount());
            this.setLastDateUpdatedInterest(today);
            this.setLastTimeUpdatedInterest(LocalTime.now());
            this.setNextDateUpdateInterest(this.lastDateUpdatedInterest.plusDays(nextDays));
        }
    }

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
            Money amount = new Money(new BigDecimal(0).subtract(this.penaltyFee.getAmount()));
            Transaction transaction = createTransaction(amount);
            transaction.setDescription("PenaltyFee");
           // addTransaction(transaction);
            return transaction;
        }else{
            return null;
        }
    }
    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        switch (this.type){
            case CHECKING:
                BigDecimal valor = new BigDecimal("12");
                if (monthlyMaintenanceFee.getAmount().compareTo(valor)>0)  {
                    this.monthlyMaintenanceFee=monthlyMaintenanceFee;
                }
                break;
        }

    }
    public void setMinimumBalance(Money minimumBalance) {
        switch (this.type){
            case SAVINGS:
                BigDecimal min=new BigDecimal("100");
                BigDecimal max=new BigDecimal("1000");
                if (minimumBalance.getAmount().compareTo(min)<=0 && minimumBalance.getAmount().compareTo(max)>=0) {
                    this.minimumBalance=minimumBalance;
                }
                break;
            case CHECKING:
                BigDecimal valor=new BigDecimal("250");
                if (minimumBalance.getAmount().compareTo(valor)>=0) {
                    this.minimumBalance=minimumBalance;
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
                BigDecimal valueMin=new BigDecimal("0.0025");
                BigDecimal valueMax=new BigDecimal("0.5");
                if (interestRate.compareTo(valueMin)>=0 && interestRate.compareTo(valueMax)<=0) {
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

        initializeDefaultValue(this.type);
    }
    private void initializeDefaultValue(AccountType accountType){
        switch (this.type){
            case CREDITCARD:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("0"));
                this.minimumBalance= new Money(new BigDecimal("0"));
                this.interestRate=new BigDecimal("0.1");
                this.creditLimit=new Money(new BigDecimal("100"));
                this.nextDateUpdateInterest=this.getCreationDate().plusDays(1);
                this.balance=this.creditLimit;
                break;
            case SAVINGS:
                this.monthlyMaintenanceFee=new Money(new BigDecimal("0"));
                this.minimumBalance=new Money(new BigDecimal("100"));
                this.interestRate=new BigDecimal("0.0025");
                this.creditLimit=new Money(new BigDecimal("0"));
                this.nextDateUpdateInterest=this.getCreationDate().plusYears(1);
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
