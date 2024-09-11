package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private LocalDate transacionDate;

    private double amount;

    private Long originId;
    private Long destinyId;
}
