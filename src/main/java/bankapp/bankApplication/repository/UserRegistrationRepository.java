package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {

}
