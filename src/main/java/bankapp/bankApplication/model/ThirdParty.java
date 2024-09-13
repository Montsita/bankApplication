package bankapp.bankApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class ThirdParty extends User  {
    private String hashedKey;

}
