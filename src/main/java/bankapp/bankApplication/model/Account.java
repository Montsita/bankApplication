package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.AccountStatus;
import bankapp.bankApplication.enums.AccountType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private AccountType type;


    private String accountNumber;
    private Money balance;// en caso creditcard Jarko
    private String secretKey;

    @ManyToOne
    private UserAccountHolder mainOwner;

    @ManyToOne
    private UserAccountHolder secondaryOwner;

    private LocalDate creationDate;
    private LocalDate lastDateUpdatedInterest; //aplicar fórmula del interés compuesto
    private final Money penaltyFee=40;

    private Money minimumBalance;
    private Money monthlyMaintenanceFee;

    //atributs de la credit card, recordar modificar el setter de la creditCard perque no te tases igual que la de estudiants
    // el setMonthlyMaintenanceFee i el minimumBalance
    private Money interestRate; // en caso creditcard Jarko
    private Money creditLimit; // en caso creditcard Jarko

    @Enumerated(EnumType.STRING)
    private AccountType accountStatus;

    @OneToMany
    private List<Transaction> transactions;

    //attribute

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
                BigDecimal valor1 = new BigDecimal("12");
                BigDecimal valor2 = new BigDecimal(monthlyMaintenanceFee.getAmount());
                if (monthlyMaintenanceFee.getAmount().compareTo(valor1))  {
                    this.monthlyMaintenanceFee.increaseAmount(monthlyMaintenanceFee.getAmount());
                }else{
                    this.monthlyMaintenanceFee=valor;
                }
                break;
            default:

        }

    }
    public void setMinimumBalance(Money minimumBalance) {
        switch (this.type){
            case STUDENTCHECKING:
                this.minimumBalance=0;
                break;
            case CREDITCARD:
                this.minimumBalance=0;
                break;
            case SAVINGS:
                if (minimumBalance >=100 && minimumBalance <=1000) {
                    this.minimumBalance = minimumBalance;
                }else{
                    this.minimumBalance=100;
                }
                break;
            case CHECKING:
                if (minimumBalance>=250) {
                    this.minimumBalance = minimumBalance;
                }else{
                    this.minimumBalance=250;
                }
                break;
            default:

        }
    }

    public void setInterestRate(Money interestRate) {
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
                this.creditLimit=0;
                break;
            case CREDITCARD:
                if (creditLimit>100 && creditLimit<=100000) {
                    this.creditLimit=creditLimit;
                }else{
                    this.creditLimit=100;
                }
                break;
            case SAVINGS:
                this.creditLimit=0;
                break;
            case CHECKING:
                this.creditLimit=0;
                break;
            default:
        }
    }

    public void setType(AccountType type) {
        if(this.type != null) return;
        // Inicializamos la cuenta a los valores por defecto
        setCreditLimit(0);
        setInterestRate(0);
        setMinimumBalance(0);
        setMonthlyMaintenanceFee(0);
    }
}
