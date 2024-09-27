package bankapp.bankApplication.model.transactions;

import bankapp.bankApplication.enums.TransactionType;
import bankapp.bankApplication.model.accounts.Account;
import bankapp.bankApplication.tools.Money;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "balance_currency"))
    })
    private Money balance;

    private LocalDate transactionDate;
    private LocalTime transactionTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount_amount")),
            @AttributeOverride(name= "currency", column = @Column(name= "amount_currency"))
    })
    private Money amount;

    private Long originId;
    private Long destinyId;
    @Enumerated(EnumType.STRING)
    private TransactionType description;
    private String concept;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name="account_id")
    private Account account;

    @Override
    public String toString() {
        System.out.println("Imprime transacc");
        return "Transaction{" +
                "id=" + id +
                ", balance=" + balance +
                ", transactionDate=" + transactionDate +
                ", transactionTime=" + transactionTime +
                ", amount=" + amount +
                ", originId=" + originId +
                ", destinyId=" + destinyId +
                ", description='" + description + '\'' +
                ", account=" + account.getId() +
                '}';
    }
}
