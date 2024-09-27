package bankapp.bankApplication.model.registrations;


import bankapp.bankApplication.model.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.ArrayList;
import java.util.Collection;

import static jakarta.persistence.FetchType.EAGER;

@Data
@Entity
@AllArgsConstructor
//@Table(name = "user_registration",
//        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "type"})})
public class UserRegistration {
    public UserRegistration(Long id, String name, String username, String password, Collection<Role> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String name;

    @Column(unique=true, nullable = false)
    private String username;

    /*@Enumerated(EnumType.STRING)
    private UserType type;
    */

    private String password;

    /**
     * The roles that the user has
     */
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @JsonBackReference
    @OneToOne(mappedBy = "userRegistration")
    private User user;

    public UserRegistration(User user) {
        setUsername("");
        this.user = user;
    }

    public UserRegistration(){
        setUsername("");
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", user=" + user +
                '}';
    }
}
