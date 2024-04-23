package com.paymybudy.repository;

import com.paymybudy.model.Beneficiaries;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiariesRepository  extends CrudRepository<Beneficiaries, Integer> {
    @Query(value = "select b.BENEFICIARY_FIRSTNAME from Beneficiaries b where b.client_id = ?1", nativeQuery = true)
    List<String> findByBeneficiary_idIn(int clientId);

    @Query(value = "select b.beneficiary_id from Beneficiaries b where b.client_id = ?1 and b.BENEFICIARY_FIRSTNAME = ?2", nativeQuery = true)
    int getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(int clientId, String beneficiaryFirstName);

    @Query(value = "select COUNT(distinct beneficiary_id) from beneficiaries where client_id = ?1 and email = ?2", nativeQuery = true)
    int TimesBeneficiaryForClientID(int clientId, String email); //needs to be only 1 as email is only allowed once

    @Query(value = "SELECT d.email\n" +
            "FROM\n" +
            "    (SELECT email\n" +
            "     FROM beneficiaries\n" +
            "     WHERE client_id = ?1) c\n" +
            "        RIGHT OUTER JOIN\n" +
            "    (SELECT email\n" +
            "     FROM beneficiaries\n" +
            "     WHERE client_id <> ?1) d\n" +
            "    ON c.email = d.email\n" +
            "where c.email is null;", nativeQuery = true)
    List<String> getBeneficiaryEmailByClientId(int clientId);

    @Query(value = "select * from beneficiaries where email = ?1 limit 1", nativeQuery = true)
    Beneficiaries getBeneficiaryFromEmailAndClientId(String email, int clientId);

    // adds a new Beneficiary, so no need of new beneficiary_id

    @Transactional
    @Modifying
    @Query(value="INSERT INTO beneficiaries (" +
            "client_id, BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME, IBAN, SWIFT, EMAIL) " +
            "VALUES(:#{#clientId}, :#{#beneficiaries.beneficiaryFirstName}, " +
            ":#{#beneficiaries.beneficiaryLastName}, " +
            ":#{#beneficiaries.iban}," +
            ":#{#beneficiaries.swift}, :#{#beneficiaries.email})", nativeQuery = true)
    void saveBeneficiaryForClientId(@Param("beneficiaries") Beneficiaries beneficiaries, @Param("clientId") int clientId) ;
}
