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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "currency"))
    })
    private Money amount;

    private Long originId;
    private Long destinyId;
}
