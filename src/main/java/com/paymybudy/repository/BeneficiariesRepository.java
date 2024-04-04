package com.paymybudy.repository;

import com.paymybudy.model.Beneficiaries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BeneficiariesRepository  extends CrudRepository<Beneficiaries, Integer> {
}
