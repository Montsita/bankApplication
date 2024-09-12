package bankapp.bankApplication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class UserAccountHolder extends User {
    private LocalDate dataOfBirth;
    @ManyToOne
    private Address primaryAddress;
    private String mailingAddress;
}
