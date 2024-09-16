package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {
    List<UserRegistration> findByUserName(String userName);
}
