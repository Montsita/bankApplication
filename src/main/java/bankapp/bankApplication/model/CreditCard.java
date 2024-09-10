package bankapp.bankApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class CreditCard extends Account{
    private float interestRate;
    private double creditLimit;



    public void setMonthlyMaintenanceFee(double monthlyMaintenanceFee) {
        this.setMonthlyMaintenanceFee(0.0);
    }

    public void setMinimumBalance(double minimumBalance) {

        this.setMinimumBalance(0.0);
    }
}
