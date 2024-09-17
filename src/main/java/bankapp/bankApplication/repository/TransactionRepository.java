package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {
    void deleteByAccountId(Long accountId);

    List<Transaction> findByAccountId(Long accountId);
}
