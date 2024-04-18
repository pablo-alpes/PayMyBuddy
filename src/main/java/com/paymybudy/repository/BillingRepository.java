package com.paymybudy.repository;

import com.paymybudy.model.Beneficiaries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends CrudRepository<Beneficiaries, Integer> {
    @Query(value="select year(date),monthname(date), SUM(ALL 0.05*(transactions.amount)) from transactions group by monthname(date), year(date)", nativeQuery = true)
    public List<Integer[]> billingFullReport();
}
