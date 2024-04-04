package com.paymybudy.repository;

import com.paymybudy.model.Accounts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Integer> {
}
