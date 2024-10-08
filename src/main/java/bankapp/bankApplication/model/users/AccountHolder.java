package bankapp.bankApplication.model.users;

import bankapp.bankApplication.model.contacts.Address;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;

@Data
@Entity
public class AccountHolder extends User {
    private LocalDate dataOfBirth;

    private String mailingAddress;

    @ManyToOne
    @JoinColumn(name="address_id")
    private Address primaryAddress;


}
