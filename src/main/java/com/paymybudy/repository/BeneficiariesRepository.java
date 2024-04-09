package com.paymybudy.repository;

import com.paymybudy.model.Beneficiaries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiariesRepository  extends CrudRepository<Beneficiaries, Integer> {
    @Query("select b.beneficiaryFirstName from Beneficiaries b where b.clientId = ?1")
    List<String> findByBeneficiary_idIn(int clientId);
}
