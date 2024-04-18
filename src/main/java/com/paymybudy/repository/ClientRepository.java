package com.paymybudy.repository;

import com.paymybudy.model.Accounts;
import com.paymybudy.model.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer>, JpaRepository<Client, Integer> {
    @Query(value = "select distinct c.client_id from client c where c.email = ?1", nativeQuery = true)
    int findByEmailIn(String email);

    @Query(value = "select * from client where email = ?1", nativeQuery = true)
    public Client findClientByEmail(String email);

    @Query(value = "select count(email) from client where email = ?1", nativeQuery = true)
    public int duplicateClientCheck(String email);
    @Transactional
    @Modifying
    @Query(value="INSERT INTO client (FIRSTNAME, LASTNAME, EMAIL, PASSWORD, ROLE" +
            ") " +
            "VALUES(" +
            ":#{#client.firstName}," +
            ":#{#client.lastName}," +
            ":#{#client.email}," +
            ":#{#client.password}," +
            ":#{#client.role})", nativeQuery = true)
    void saveClient(@Param("client") Client client);

}
