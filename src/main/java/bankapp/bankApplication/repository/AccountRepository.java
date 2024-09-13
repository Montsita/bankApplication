package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.Account;
import bankapp.bankApplication.model.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>  {
}
