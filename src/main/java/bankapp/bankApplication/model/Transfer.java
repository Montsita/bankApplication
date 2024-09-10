package bankapp.bankApplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

   // @OneToOne
   // private Account accountOrigin;
   // @OneToOne
   // private Account accountDestination;

    private double amount;
    private String description;

}
