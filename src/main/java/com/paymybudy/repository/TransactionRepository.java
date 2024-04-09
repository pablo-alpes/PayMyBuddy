package com.paymybudy.repository;

import com.paymybudy.model.Transactions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface TransactionRepository extends CrudRepository<Transactions, Integer> {

    /*public void executeTransaction(int client_ID, int beneficiary_ID, int amount, String description) {
    }
    public void getTransactionHistory(int client_ID, Date dateFrom, Date dateTo) {
    }
    public void rollbackTransaction(int transaction_ID) {
    }
    */

    @Query(value = "select t.transaction_id, t.client_id, t.beneficiary_id, t.amount, t.description, t.date, beneficiaries.beneficiary_firstname, t.status from transactions t LEFT JOIN beneficiaries ON beneficiaries.client_id = t.client_id;", nativeQuery = true)
        //the query asks the DB for all the lines matching the criteria = ?1
    Iterable<Transactions> findByClient_idInOrderByDateDesc(int clientId);
}
