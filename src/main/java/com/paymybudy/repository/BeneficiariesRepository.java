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
    @Query("select b.beneficiaryFirstName from Beneficiaries b where b.clientId = ?1")
    List<String> findByBeneficiary_idIn(int clientId);

    @Query("select b.beneficiaryId from Beneficiaries b where b.clientId = ?1 and b.beneficiaryFirstName = ?2")
    int getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(int clientId, String beneficiaryFirstName);

    @Query(value = "select COUNT(distinct beneficiary_id) from beneficiaries where client_id = ?1 and email = ?2", nativeQuery = true)
    int TimesBeneficiaryForClientID(int clientId, String email); //needs to be only 1 as email is only allowed once

    @Query("select b.email from Beneficiaries b where b.clientId <> ?1")
    List<String> getBeneficiaryEmailByClientId(int clientId);

    @Query(value = "select * from beneficiaries where email = ?1 limit 1", nativeQuery = true)
    Beneficiaries getBeneficiaryFromEmailAndClientId(String email, int clientId);

    // adds a new Beneficiary, so no need of new beneficiary_id
    @Modifying
    @Transactional
    @Query(value="INSERT INTO beneficiaries (" +
            "client_id, BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME, IBAN, SWIFT, EMAIL) " +
            "VALUES(:#{#clientId}, :#{#beneficiaries.beneficiaryFirstName}, " +
            ":#{#beneficiaries.beneficiaryLastName}, " +
            ":#{#beneficiaries.iban}," +
            ":#{#beneficiaries.swift}, :#{#beneficiaries.email})", nativeQuery = true)
    void saveBeneficiaryForClientId(@Param("beneficiaries") Beneficiaries beneficiaries, @Param("clientId") int clientId) ;
}
