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
    @Query(value = "select * from (select distinct t.transaction_id, t.client_id, t.beneficiary_id, t.amount, t.description, t.date, beneficiaries.beneficiary_firstname, t.status from transactions t LEFT JOIN beneficiaries ON beneficiaries.client_id = t.client_id AND beneficiaries.beneficiary_id = t.beneficiary_id) db where db.client_id = ?1", nativeQuery = true)
        //the query asks the DB for all the lines matching the criteria clientid = ?1
    Iterable<Transactions> findByClient_idInOrderByDateDesc(int clientId);
}
