package bankapp.bankApplication.repository.impl;

import bankapp.bankApplication.model.transactions.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {

    @Modifying
    @Transactional
    void deleteByAccountId(Long accountId);

    List<Transaction> findByAccountId(Long accountId);

    //List<Transaction> findFirstByAccountIdOrderByTransactionDateDescTransactionTimeDesc(Long accountId);
    @Query(value = "SELECT * FROM transaction WHERE account_id = :accountId ORDER BY transaction_date DESC, transaction_time DESC LIMIT 1", nativeQuery = true)
    Transaction findLastTransactionByAccountId(@Param("accountId") Long accountId);

}
