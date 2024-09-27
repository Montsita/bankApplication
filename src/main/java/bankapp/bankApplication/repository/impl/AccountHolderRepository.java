package bankapp.bankApplication.repository.impl;

import bankapp.bankApplication.model.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long>  {
}
