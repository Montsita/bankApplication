package bankapp.bankApplication.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
public class AccountHolder  extends User {
    private LocalDate dataOfBirth;
    @ManyToOne
    private Address primaryAddress;
    private String mailingAddress;
}
