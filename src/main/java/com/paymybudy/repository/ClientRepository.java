package com.paymybudy.repository;

import com.paymybudy.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer>, JpaRepository<Client, Integer> {
    @Query("select distinct c.client_id from Client c where c.email = ?1")
    int findByEmailIn(String email);

    @Query(value = "select * from client where email = ?1", nativeQuery = true)
    public Client findClientByEmail(String email);

}
