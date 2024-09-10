package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private double balance;
    private String secretKey;

    @ManyToOne
    private AccountHolder mainOwner;

    @ManyToOne
    private AccountHolder secondaryOwner;

    private LocalDate creationDate;
    private double penaltyFee;
    private double minimumBalance;
    private double monthlyMaintenanceFee;


    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany
    private List<Transaction> transactions;
}
