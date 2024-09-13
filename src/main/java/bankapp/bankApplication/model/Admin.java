package bankapp.bankApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Admin extends User{

    @Override
    public String toString() {
        return "Admin{}:" + this.getId();
    }
}
