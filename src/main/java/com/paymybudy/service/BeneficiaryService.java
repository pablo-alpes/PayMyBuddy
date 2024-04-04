package com.paymybudy.service;

import com.paymybudy.model.Beneficiaries;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiaryService { // to confirm spec on what key to use to retrieve them
    public void addBeneficiary(int clientID, String firstName, String lastName, String email) {
    }

    //public void removeBeneficiary(int clientID, String firstName, String lastName, String beneficiaryEmail) {}

    public List<Beneficiaries> getBeneficiaries(String clientID) {
        return null;
    }

}
