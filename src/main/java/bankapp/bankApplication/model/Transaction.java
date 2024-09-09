package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.AccountTransaction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountTransaction accountTransaction;

    private LocalDate transacionDate;

    private double quantity;

}
