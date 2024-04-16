package com.paymybudy.repository;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Beneficiaries;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Integer> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE prod.accounts a SET a.balance = ?1 WHERE a.account_ID=?2", nativeQuery = true)
    void updateBalance(float balance, int accountId);

    @Query(value = "select distinct a.ACCOUNT_ID from accounts a where a.CLIENT_ID = ?1", nativeQuery = true)
    int findAccountsByClientID(int clientId);

    @Query(value = "select count(CLIENT_ID) from accounts where CLIENT_ID = ?1", nativeQuery=true)
    int duplicateAccountCheck(int clientId);

    @Modifying
    @jakarta.transaction.Transactional
    @Query(value="INSERT INTO accounts (" +
            "client_id, BALANCE, IBAN, SWIFT) " +
            "VALUES(:#{#clientId}, " +
            ":#{#accounts.balance}," +
            ":#{#accounts.iban}," +
            ":#{#accounts.swift})", nativeQuery = true)
    void saveAccount(@Param("accounts") Accounts accounts, @Param("clientId") int clientId) ;
}
