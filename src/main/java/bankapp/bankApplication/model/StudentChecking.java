package bankapp.bankApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class StudentChecking extends Account{


    public void setMonthlyMaintenanceFee(double monthlyMaintenanceFee) {
        this.setMonthlyMaintenanceFee(0);
    }

    public void setMinimumBalance(double minimumBalance) {
        this.setMinimumBalance(0);
    }
}
