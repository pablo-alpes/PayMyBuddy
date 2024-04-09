package com.paymybudy.model;

import jakarta.persistence.*;

@Entity
@Table(name="beneficiaries")
public class Beneficiaries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BENEFICIARY_ID")
    private int beneficiaryId;
    @Column(name = "CLIENT_ID")
    private int clientId;
    @Column(name = "BENEFICIARY_FIRSTNAME")
    String beneficiaryFirstName;
    @Column(name = "BENEFICIARY_LASTNAME")
    String beneficiaryLastName;
    @Column(name = "IBAN")
    String iban;
    @Column(name = "SWIFT")
    String swift;

    public Beneficiaries() {
    }

    public Beneficiaries(int beneficiaryId, int clientId, String beneficiaryFirstName, String beneficiaryLastName, String iban, String swift) {
        this.beneficiaryId = beneficiaryId;
        this.clientId = clientId;
        this.beneficiaryFirstName = beneficiaryFirstName;
        this.beneficiaryLastName = beneficiaryLastName;
        this.iban = iban;
        this.swift = swift;
    }

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getBeneficiaryFirstName() {
        return beneficiaryFirstName;
    }

    public void setBeneficiaryFirstName(String beneficiaryFirstName) {
        this.beneficiaryFirstName = beneficiaryFirstName;
    }

    public String getBeneficiaryLastName() {
        return beneficiaryLastName;
    }

    public void setBeneficiaryLastName(String beneficiaryLastName) {
        this.beneficiaryLastName = beneficiaryLastName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
