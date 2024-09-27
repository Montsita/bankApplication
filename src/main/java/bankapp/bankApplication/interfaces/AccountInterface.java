package bankapp.bankApplication.interfaces;

import bankapp.bankApplication.model.transactions.Transaction;

public interface AccountInterface {
    void addTransaction(Transaction transaction);
}
