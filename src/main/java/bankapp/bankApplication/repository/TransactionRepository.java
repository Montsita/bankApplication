package bankapp.bankApplication.repository;

import bankapp.bankApplication.model.AccountHolder;
import bankapp.bankApplication.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {

    @Modifying
    @Transactional
    void deleteByAccountId(Long accountId);

    List<Transaction> findByAccountId(Long accountId);
}
