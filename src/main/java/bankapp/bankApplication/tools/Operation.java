package bankapp.bankApplication.tools;

import bankapp.bankApplication.enums.InterestType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Operation {
     public static  ResInterestCalculation interestCalculation(LocalDate initialDate, LocalDate finalDate, BigDecimal amount, BigDecimal interestRate, InterestType type){
         BigDecimal calculation=new BigDecimal(0) ;
         BigDecimal rate=new BigDecimal(BigInteger.ZERO);
         BigDecimal aux =new BigDecimal(BigInteger.ZERO);
         LocalDate nextUpdate=null;
         ResInterestCalculation res = null;
         if (initialDate.isBefore(finalDate)|| initialDate.isEqual(finalDate)){
             switch (type){
                 case DIARY:
                     long days = ChronoUnit.DAYS.between(initialDate, finalDate)+1;
                     rate=interestRate.divide(new BigDecimal("365"),6, RoundingMode.DOWN);
                     for (int i = 1; i <= days; i++) {
                         calculation = calculation.add( amount.multiply(rate));
                     }
                     calculation =calculation.setScale(2, RoundingMode.DOWN);
                     res= new ResInterestCalculation(calculation, initialDate.plusDays(days));
                     break;

                 case MONTHLY:
                     long months = ChronoUnit.MONTHS.between(initialDate, finalDate)+1;
                     rate=interestRate.divide(new BigDecimal("12"),6, RoundingMode.DOWN);
                     for (int i = 1; i <= months; i++) {
                         calculation = calculation.add( amount.multiply(rate));
                     }
                     calculation =calculation.setScale(2, RoundingMode.DOWN);
                     res= new ResInterestCalculation(calculation, initialDate.plusMonths(months));
                     break;

                 case ANNUAL:
                     long years = ChronoUnit.YEARS.between(initialDate, finalDate);
                     rate=interestRate;
                     for (int i = 1; i <= years; i++) {
                         calculation = calculation.add( amount.multiply(rate));
                     }
                     calculation =calculation.setScale(2, RoundingMode.DOWN);
                     res= new ResInterestCalculation(calculation, initialDate.plusMonths(years));
                     break;

                 default:
             }
         }

         return res;

     }

}

