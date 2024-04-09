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
    } // to confirm spec on what key to use to retrieve them
    public void addBeneficiary(int clientID, String firstName, String lastName, String iban, String swift) {
        //Rules : An unique mail for the key firstName-lastName is accepted
        Beneficiaries beneficiary = new Beneficiaries();

        //values of the beneficiary to add -- beneficiary Id is autoincremented
        beneficiary.setClientId(clientID);
        beneficiary.setBeneficiaryFirstName(firstName);
        beneficiary.setBeneficiaryLastName(lastName);
        beneficiary.setIban(iban);
        beneficiary.setSwift(swift);

        //we add the new record and we save it
        beneficiariesRepository.save(beneficiary);

    }

    //public void removeBeneficiary(int clientID, String firstName, String lastName, String beneficiaryEmail) {}

    public List<Beneficiaries> getBeneficiaries(String clientID) {
        return null;
    }

}
