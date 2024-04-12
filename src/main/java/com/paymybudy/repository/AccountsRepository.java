package com.paymybudy.repository;

import com.paymybudy.model.Accounts;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Accounts a SET a.balance = ?1 WHERE a.account_ID=?2")
    void updateBalance(float balance, int accountId);

    @Query(value = "select distinct a.ACCOUNT_ID from accounts a where a.CLIENT_ID = ?1", nativeQuery = true)
    int findAccountsByClientID(int clientId);
}
