package com.paymybudy.repository;

import com.paymybudy.model.Beneficiaries;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class BeneficiaryAddFormDTO implements Serializable {
    private Beneficiaries beneficiaries;
    private String selectedEmail;

    public BeneficiaryAddFormDTO() { }
    public BeneficiaryAddFormDTO(Beneficiaries beneficiaries, String selectedEmail) {
        this.beneficiaries = beneficiaries;
        this.selectedEmail = selectedEmail;
    }

    public Beneficiaries getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(Beneficiaries beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public String getSelectedEmail() {
        return selectedEmail;
    }

    public void setSelectedEmail(String selectedEmail) {
        this.selectedEmail = selectedEmail;
    }
}
