package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.UserAccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<UserAccountHolder, Long>  {
}
