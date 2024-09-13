package bankapp.bankApplication.model;

import bankapp.bankApplication.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    @OneToOne(mappedBy = "userRegistration")
    private User user;

    public UserRegistration(User user) {
        setUserName("");
        this.user = user;
    }

    public UserRegistration(){
        setUserName("");
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", type=" + type +
                ", user=" + user +
                '}';
    }
}
