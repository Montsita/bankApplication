package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "user_registration",
//        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "type"})})
public class UserRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(unique=true, nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserType type;


    private User user;

    public UserRegistration(User user) {
        this.user = user;
    }
}
