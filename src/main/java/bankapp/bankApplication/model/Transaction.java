package bankapp.bankApplication.model;

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

    private LocalDate transacionDate;

    private double amount;

    private Long originId;
    private Long destinyId;
}
