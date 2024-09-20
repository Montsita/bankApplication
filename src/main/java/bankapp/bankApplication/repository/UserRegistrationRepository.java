package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {
    Optional<UserRegistration> findByUserName(String userName);
}
