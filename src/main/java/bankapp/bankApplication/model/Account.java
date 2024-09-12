package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.AccountStatus;
import bankapp.bankApplication.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@MappedSuperclass
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated
    private AccountType type;
    private String accountNumber;
    private double balance;// en caso creditcard Jarko
    private String secretKey;

    @ManyToOne
    private AccountHolder mainOwner;

    @ManyToOne
    private AccountHolder secondaryOwner;

    private LocalDate creationDate;
    private final double penaltyFee=40;

    private double minimumBalance;
    private double monthlyMaintenanceFee;

    //atributs de la credit card, recordar modificar el setter de la creditCard perque no te tases igual que la de estudiants
    // el setMonthlyMaintenanceFee i el minimumBalance
    private float interestRate; // en caso creditcard Jarko

    private LocalDate lastDateUpdatedInterest; // en caso creditcard Jarko
    private double creditLimit; // en caso creditcard Jarko

    @Enumerated(EnumType.STRING)
    private AccountType accountStatus;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountType;

    @OneToMany
    private List<Transaction> transactions;

    //attribute

    public void setType(AccountType type) {
        this.type = type;
    }



    public void setMonthlyMaintenanceFee(double monthlyMaintenanceFee) {
        switch (this.type){
            case STUDENTCHECKING:
                this.monthlyMaintenanceFee=0;
                break;
            case CREDITCARD:
                this.monthlyMaintenanceFee=0;
                break;
            case SAVINGS:
                this.monthlyMaintenanceFee=0;
                break;
            case CHECKING:
                if (monthlyMaintenanceFee>=12) {
                    this.monthlyMaintenanceFee = monthlyMaintenanceFee;
                }else{
                    this.monthlyMaintenanceFee=12;
                }
                break;
            default:

        }

    }
    public void setMinimumBalance(double minimumBalance) {
        switch (this.type){
            case STUDENTCHECKING:
                this.monthlyMaintenanceFee=0;
                break;
            case CREDITCARD:
                this.monthlyMaintenanceFee=0;
                break;
            case SAVINGS:
                this.monthlyMaintenanceFee=0;
                break;
            case CHECKING:
                if (minimumBalance>=250) {
                    this.monthlyMaintenanceFee = monthlyMaintenanceFee;
                }else{
                    this.monthlyMaintenanceFee=250;
                }
                break;
            default:

        }
    }

    public void setInterestRate(float interestRate) {
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
}
