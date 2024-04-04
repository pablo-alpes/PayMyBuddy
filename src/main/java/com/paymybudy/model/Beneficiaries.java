package com.paymybudy.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JoinColumnOrFormula;

@Entity
@Table(name="beneficiaries")
public class Beneficiaries {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="BENEFICIARY_ID")
    /*@OneToMany(
            cascade  = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "beneficiaries_name",
            joinColumns = @JoinColumn(name = "beneficiary_ID"),
            inverseJoinColumns = @JoinColumn(name = "beneficiary_firstName")
    )
    */

    private int beneficiary_id;
    @Column(name="CLIENT_ID")
    private int client_id;
    @Column(name="BENEFICIARY_FIRSTNAME")
    String beneficiary_firstName;
    @Column(name="BENEFICIARY_LASTNAME")
    String beneficiary_lastName;
    @Column(name="IBAN")
    String iban;
    @Column(name="SWIFT")
    String swift;

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
