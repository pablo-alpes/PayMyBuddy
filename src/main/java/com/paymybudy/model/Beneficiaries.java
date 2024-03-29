package com.paymybudy.model;

public class Beneficiaries {
    private int beneficiary_id;
    private int client_id;

    String beneficiary_firstName;
    String beneficiary_lastName;

    public Beneficiaries() {
    }

    public Beneficiaries(int beneficiary_id, int client_id, String beneficiary_firstName, String beneficiary_lastName) {
        this.beneficiary_id = beneficiary_id;
        this.client_id = client_id;
        this.beneficiary_firstName = beneficiary_firstName;
        this.beneficiary_lastName = beneficiary_lastName;
    }

    public int getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(int beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getBeneficiary_firstName() {
        return beneficiary_firstName;
    }

    public void setBeneficiary_firstName(String beneficiary_firstName) {
        this.beneficiary_firstName = beneficiary_firstName;
    }

    public String getBeneficiary_lastName() {
        return beneficiary_lastName;
    }

    public void setBeneficiary_lastName(String beneficiary_lastName) {
        this.beneficiary_lastName = beneficiary_lastName;
    }
}
