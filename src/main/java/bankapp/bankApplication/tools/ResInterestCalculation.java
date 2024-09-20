package bankapp.bankApplication.tools;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResInterestCalculation {
    private BigDecimal calculation=BigDecimal.ZERO;
    private LocalDate nextDateCalculation;

    public ResInterestCalculation(BigDecimal calculation, LocalDate nextDateCalculation) {
        this.calculation=calculation;
        this.nextDateCalculation=nextDateCalculation;
    }
    public ResInterestCalculation() {
    }



    @Override
    public String toString() {
        return "ResInterestCalculation{" +
                "calculation=" + calculation +
                ", nextCalculation=" + nextDateCalculation +
                '}';
    }

    public BigDecimal getCalculation() {
        return calculation;
    }

    public LocalDate getNextDateCalculation() {
        return nextDateCalculation;
    }




}
