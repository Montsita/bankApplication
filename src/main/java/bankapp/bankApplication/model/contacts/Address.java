package bankapp.bankApplication.model.contacts;

import bankapp.bankApplication.model.users.AccountHolder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String entrancePortal;
    private String district;
    private String postalCode;
    private String city;
    private String country;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountHolder> accountHolder;
}
