package bankapp.bankApplication.model.users;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Admin extends User {

    @Override
    public String toString() {
        return "Admin{}:" + this.getId();
    }
}
