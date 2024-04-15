package com.paymybudy.service;

import com.paymybudy.model.Beneficiaries;
import com.paymybudy.repository.BeneficiariesRepository;
import jakarta.persistence.Column;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService {

    private final BeneficiariesRepository beneficiariesRepository;

    public BeneficiaryService(BeneficiariesRepository beneficiariesRepository) {
        this.beneficiariesRepository = beneficiariesRepository;
    }


    public void addBeneficiary(int clientID, String firstName, String lastName, String iban, String swift, String email) {
        //Rules : An unique mail for the key firstName-lastName is accepted
        if (TimesBeneficiaryForClientID(clientID, email)==0) { //checks if no duplication of beneficiary record
            Beneficiaries beneficiary = new Beneficiaries();

            //values of the beneficiary to add -- beneficiary Id is autoincremented
            beneficiary.setClientId(clientID);
            beneficiary.setBeneficiaryFirstName(firstName);
            beneficiary.setBeneficiaryLastName(lastName);
            beneficiary.setIban(iban);
            beneficiary.setSwift(swift);
            beneficiary.setEmail(email);

            //we add the new record and we save it
            beneficiariesRepository.save(beneficiary);
        }
    }
    public void addExistingBeneficiaryToClientId(Beneficiaries beneficiary, int clientId) {
        if (TimesBeneficiaryForClientID(clientId, beneficiary.getEmail())==0) { //checks if no duplication of beneficiary record
            beneficiariesRepository.saveBeneficiaryForClientId(beneficiary, clientId);
        }
    } //required to be adapted to the object to the addition


    public int getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(int clientId, String firstName) {
        return beneficiariesRepository.getBeneficiaryIdFromKeyClientIDAndBeneficiaryFirstName(clientId, firstName);
    }

    public List<String> getBeneficiaryEmailByClientId(int clientId) {
        return beneficiariesRepository.getBeneficiaryEmailByClientId(clientId);
    }

    public int TimesBeneficiaryForClientID(int clientId, String email) {
        //determines how many times a beneficiary is assigned to a client
        //assuming only one single beneficiary can be assigned per key client-beneficiary)
        return beneficiariesRepository.TimesBeneficiaryForClientID(clientId, email);
    }

    public Beneficiaries getBeneficiaryFromEmailAndClientId(String beneficiaryEmail, int clientId) {
        return beneficiariesRepository.getBeneficiaryFromEmailAndClientId(beneficiaryEmail, clientId);
    }
}

